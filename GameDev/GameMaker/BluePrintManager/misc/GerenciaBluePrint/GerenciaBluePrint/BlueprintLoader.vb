Imports System.Data.OleDb
Imports System.Data.SqlClient
Imports System.Data.Common
Imports Microsoft.VisualBasic

Imports System.IO
Imports System.Security.AccessControl
Imports System.Security.Principal
Imports System.Management
Imports System.Runtime.InteropServices
Imports System.Net.NetworkInformation

Imports System.Xml
Imports System.Text
Imports System
Imports System.Net

Public Class BlueprintLoader
    Dim arrDefines As ArrayList = New ArrayList()
    Dim arrBlueprints As ArrayList = New ArrayList()

    Dim flagParseBlueprint As Boolean = False
    Dim txtBP As String = ""
    Public rootdir As String
    Public rootfile As String
    Dim local As LocalClient
    Dim dataModificacao As Date
    Dim okToSave As Boolean = True
    Dim frm As FrmPrincipal
    Dim baseElement As String

    Dim flagComentario As Boolean = False

    Public Sub New(local As LocalClient, rootdir As String, rootfile As String, baseElement As String, frm As FrmPrincipal)
        Me.rootdir = rootdir
        Me.rootfile = rootfile
        Me.local = local
        Me.frm = frm
        Me.baseElement = baseElement
        carregaBlueprint(rootdir, rootfile)

        linkaBlueprints()
        atualizaDataModificacao()
    End Sub

    Function arquivoMudou() As Boolean
        Dim dataAtual As Date = local.getDataModificacao(rootdir & "\" & rootfile)
        If (dataAtual.Second <> dataModificacao.Second) Or (dataAtual.Minute <> dataModificacao.Minute) Then
            Return True
        End If
        Return False
    End Function

    Public Sub salva(rootdir As String, rootfile As String)
        If arrBlueprints.Count = 0 Then Return
        If (Not okToSave) Then Return
        Dim objW As New StreamWriter(rootdir & "\" & rootfile, False)
        objW.WriteLine("//Defines")
        gravaDefines(objW)
        objW.WriteLine("")
        objW.WriteLine("//Blueprints")

        Dim root = arrBlueprints.Item(0)
        gravaBlueprints(root, objW, False)

        objW.Close()
        atualizaDataModificacao()

    End Sub

    Sub atualizaDataModificacao()
        dataModificacao = local.getDataModificacao(rootdir & "\" & rootfile)
    End Sub

    Sub gravaDefines(objW As StreamWriter)
        Dim define As Define
        For Each define In arrDefines
            objW.WriteLine(define.generate())
        Next
    End Sub

    Sub gravaBlueprints(root As Blueprint, objW As StreamWriter, commentName As Boolean)
        Dim bp As Blueprint
        
        objW.WriteLine(root.generate())
        For Each bp In root.getChildren
            If bp.size() > 5 Then
                '    commentName = True
            End If

            If commentName Then
                objW.WriteLine("// " & bp.nome & " //")
                Dim h As String = bp.getHierarquia("_").Replace("_base", "")
                Dim nome As String = "BluePrint" & h & "_" & bp.nome & ".txt"
                objW.WriteLine("@include " & nome)

                Dim objW2 As New StreamWriter(rootdir & "\" & nome, False)
                gravaBlueprints(bp, objW2, False)
                objW2.Close()
            Else
                gravaBlueprints(bp, objW, False)
            End If


        Next
    End Sub

    Sub linkaBlueprints()
        Dim bp As Blueprint
        For Each bp In arrBlueprints
            bp.limpaChildren()
        Next

        For Each bp In arrBlueprints
            Dim bpPai As Blueprint = getBPWithName(bp.extends)
            If (Not bpPai Is Nothing) Then
                bpPai.addChild(bp)
            End If
        Next
    End Sub

    Sub atualizaDefines(txtDefines As String)
        arrDefines.Clear()
        txtDefines = txtDefines & Chr(13) & Chr(10)
        While txtDefines.Trim.Length > 0
            Dim def As String = txtDefines.Substring(0, txtDefines.IndexOf(Chr(13))).Trim
            Dim defOrig As String = def
            If (def <> "") Then
                Dim tipo As String = def.Substring(0, def.IndexOf(" "))
                def = def.Substring(def.IndexOf(" ") + 1)
                Dim nome As String = def.Substring(0, def.IndexOf("="))
                Dim valor As String = def.Substring(def.IndexOf("=") + 1)
                addDefine(tipo, nome, valor)
            End If

            txtDefines = txtDefines.Replace(defOrig, "")
            txtDefines = txtDefines.Trim & Chr(13)
        End While

    End Sub

    Function getBPWithName(name As String) As Blueprint
        Dim bp As Blueprint
        For Each bp In arrBlueprints
            If (bp.nome = name) Then
                Return bp
            End If
        Next
        Return Nothing
    End Function

    Function count() As Integer
        Return arrBlueprints.Count
    End Function

    Public Sub addDefine(tipo As String, name As String, value As String)
        Dim def As New Define(tipo.Trim, name.Trim, value.Trim)
        arrDefines.Add(def)
    End Sub

    Function getDefines() As ArrayList
        Return arrDefines
    End Function

    Sub carregaBlueprint(rootdir As String, rootfile As String)
        Dim objReader As New StreamReader(rootdir & "\" & rootfile)
        Dim sLine As String = ""
        frm.limpaTodo()

        okToSave = True
        flagComentario = False
        Do
            sLine = objReader.ReadLine()
            If Not sLine Is Nothing Then
                sLine = sLine.Trim
                If (sLine <> "") Then
                    If (Not parseia(sLine, rootdir)) Then
                        objReader.Close()
                        okToSave = False
                        Return
                    End If
                End If
            End If
        Loop Until sLine Is Nothing
        objReader.Close()
    End Sub

    Function parseia(sLine As String, rootdir As String) As Boolean
        If sLine = "*/" Then
            flagComentario = False
            sLine = ""
        End If

        If sLine = "/*" Then
            flagComentario = True
            sLine = ""
        End If
        If sLine.StartsWith("//") Then sLine = ""
        Dim coment As String = ""

        sLine = sLine.Trim

        If (sLine = "") Then Return True

        If Not sLine.StartsWith("@") Then
            sLine = "@" & sLine
        End If

        If Not flagComentario Then
            If (Not flagParseBlueprint) Then
                parseiaLinhaUtil(sLine)
            Else
                txtBP = txtBP & Chr(13) & sLine
                If sLine = "@end" Then
                    flagParseBlueprint = False
                    If (Not adicionaNewBlueprint(txtBP)) Then
                        Return False
                    End If
                End If
            End If


        End If

        Return True
    End Function

    Sub parseiaLinhaUtil(sLine As String)

      
        If (sLine.StartsWith("@include")) Then
            sLine = sLine.Replace("@include ", "")
            carregaBlueprint(rootdir, sLine)
        End If
        If (sLine.StartsWith("@define")) Then
            sLine = sLine.Replace("@define ", "")
            Dim nome = sLine.Substring(0, sLine.IndexOf("="))
            Dim valor = sLine.Substring(sLine.IndexOf("=") + 1)
            addDefine("@define", nome, valor)
        End If

        If (sLine.StartsWith("@function")) Then
            sLine = sLine.Replace("@function ", "")
            Dim nome = sLine.Substring(0, sLine.IndexOf("="))
            Dim valor = sLine.Substring(sLine.IndexOf("=") + 1)
            addDefine("@function", nome, valor)
        End If

        If sLine.StartsWith("@mod") Or sLine.StartsWith("@factory") Or (sLine.StartsWith("@blueprint")) Then
            flagParseBlueprint = True
            txtBP = sLine

        End If
    End Sub

   

    Function adicionaNewBlueprint(txtBP As String)
       
        Dim bp As Blueprint = BluePrintController.parseBlueprint(txtBP, frm)

        Return addBlueprint(bp)
    
    End Function

    Public Function addBlueprint(bp As Blueprint)
        Dim bpC As Blueprint
        For Each bpC In arrBlueprints
            If (bp.nome = bpC.nome) Then
                MsgBox("Blueprint com nome repetido:" & bp.nome & ". Cancelando... ")
                Return False
            End If

        Next
        If Not bp.extends Is Nothing Then
            Dim pai As Blueprint = getBPWithName(bp.extends)
            If Not pai Is Nothing Then
                pai.addChild(bp)

            End If
        End If
        arrBlueprints.Add(bp)
        Return True
    End Function

    Public Sub removeBlueprint(nome As String)
        Dim bpC As Blueprint
        For Each bpC In arrBlueprints
            If (nome = bpC.nome) Then
                arrBlueprints.Remove(bpC)
                bpC.remove()
                linkaBlueprints()
                Return
            End If

        Next

    End Sub

    Function filtraPorTag(txtTags As String)
        Dim arrTags As String() = txtTags.Split(" ")
        Dim arrReturn As ArrayList = New ArrayList
        Dim bp As Blueprint
        For Each bp In arrBlueprints
            Dim tag As String
            Dim contain As Boolean = True
            For Each tag In arrTags
                If tag <> "" Then
                    If Not bp.containsTag(tag) Then contain = False
                End If
            Next
            If contain Then
                arrReturn.Add(bp)
            End If

        Next

        Return arrReturn
    End Function
End Class
