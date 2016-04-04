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

Public Class Modulo
    Dim nome As String
    Dim pasta As String
    Dim loaderFile As String
    Dim buildDir As String
    Dim flagRemoveComentario As Boolean = False
    Dim sufixo As String

    Dim local As LocalClient = New LocalClient

    Dim arrCSS As ArrayList = New ArrayList
    Dim arrPHP As ArrayList = New ArrayList
    Dim arrJS As ArrayList = New ArrayList

    Const TIPO_PHP = 1
    Const TIPO_JS = 2
    Const TIPO_CSS = 3

    Dim analiseCSS As IAnalise
    Dim analiseJS As IAnalise
    Dim analisePHP As IAnalise

    Sub setAnalises(analiseCSS As IAnalise, analiseJS As IAnalise, analisePHP As IAnalise)
        Me.analiseCSS = analiseCSS
        Me.analiseJS = analiseJS
        Me.analisePHP = analisePHP
    End Sub

    Sub setSufixo(suf As String)
        sufixo = suf
    End Sub

    Public Sub New(nome As String, pasta As String, loaderFile As String, buildDestination As String)
        Me.nome = nome
        Me.pasta = pasta
        Me.loaderFile = loaderFile
        Me.buildDir = buildDestination


    End Sub

    Public Sub comprimeModulo()
        relacionaArquivos()
        juntaComponentes()
        juntaFinal(buildDir, nome, "minified.php" & sufixo)
        juntaFinal(buildDir, nome, "minified.js" & sufixo)
        juntaFinal(buildDir, nome, "minified.css" & sufixo)


    End Sub

    

    Sub juntaFinal(buildDir As String, modulo As String, arquivo As String)
        If (Not local.FileExists(buildDir & "\" & modulo & "\" & arquivo)) Then
            Return
        End If
        Dim objReader As New StreamReader(buildDir & "\" & modulo & "\" & arquivo)
        Dim sLine As String = ""
        Dim objW As New StreamWriter(buildDir & "\" & arquivo, True)
        Do
            sLine = objReader.ReadLine()
            If Not sLine Is Nothing Then
                sLine = sLine.Trim
                If (sLine <> "") Then
                 
                    objW.WriteLine(sLine)
                End If
            End If
        Loop Until sLine Is Nothing
        objW.Close()
        objReader.Close()
    End Sub

    Sub juntaComponentes()

        local.createFolder(buildDir & "\" & nome)

        juntaArray(analisePHP, arrPHP, buildDir & "\" & nome & "\" & "minified.php" & sufixo)
        juntaArray(analiseJS, arrJS, buildDir & "\" & nome & "\" & "minified.js" & sufixo)
        juntaArray(analiseCSS, arrCSS, buildDir & "\" & nome & "\" & "minified.css" & sufixo)


    End Sub
    Sub juntaArray(analise As IAnalise, arr As ArrayList, arquivoDestino As String)
        Dim arquivo
        If arr.Count = 0 Then Return

        Dim objW As New StreamWriter(arquivoDestino, False)
        For Each arquivo In arr
            juntaArquivo(analise, arquivo, objW)
        Next
        objW.Close()

    End Sub

    Sub juntaArquivo(analise As IAnalise, arquivo As String, objW As StreamWriter)
        'objW.WriteLine("/* SOURCE: " & nome & "/" & arquivo & "  */")
        Dim objReader As New StreamReader(pasta & "\" & arquivo)
        Dim sLine As String = ""
        Dim firstLine As String = ""
        analise.iniciaNovoArquivo()
        Dim flagComentario As Boolean = False
        Do
            sLine = objReader.ReadLine()
            If Not sLine Is Nothing Then
                analise.adicionaLinha(sLine)

                sLine = sLine.Trim

                sLine = removeComentariosSingleLine(sLine)
                If flagRemoveComentario Then

                    If sLine.Contains("/*") Then
                        flagComentario = True
                        Dim comentIni = sLine.IndexOf("/*")
                        sLine = sLine.Substring(0, comentIni)

                    End If
                    If flagComentario And Not sLine.Contains("*/") Then
                        sLine = ""
                    End If
                    If sLine.Contains("*/") Then
                        Dim comentFim = sLine.IndexOf("*/")
                        sLine = sLine.Substring(comentFim + 2)
                        flagComentario = False
                    End If
                End If

                sLine = sLine.Trim


                If (sLine <> "") Then
                    objW.WriteLine(sLine & " ")
                End If
            End If
        Loop Until sLine Is Nothing

        analise.fimArquivo(arquivo)
        objReader.Close()

    End Sub

    Function removeComentariosSingleLine(sLine As String) As String
        If (sLine.StartsWith("//")) Then sLine = ""
        sLine = " " & sLine & " "

        Dim comentBarra = sLine.IndexOf("//")

        If sLine.Contains("section*/") Then
            Console.WriteLine("break")
        End If
        If (comentBarra > 0) And Not sLine.Contains("://") Then
            sLine = sLine.Substring(0, comentBarra)
        End If


        Dim comentIni = sLine.IndexOf("/*")
        Dim comentFim = sLine.IndexOf("*/")
        If (comentFim > comentIni) And comentFim > 0 And comentIni >= 0 Then
            '  Console.WriteLine(sLine)
            Dim inicio As String = ""
            If (comentIni > 0) Then
                inicio = sLine.Substring(0, comentIni)
            End If
            sLine = inicio & sLine.Substring(comentFim + 2)
        End If
        Return sLine.Trim
    End Function

    Sub relacionaArquivos()
        Dim objReader As New StreamReader(pasta & "\" & loaderFile)
        Dim sLine As String
        Do
            sLine = objReader.ReadLine()
            If Not sLine Is Nothing Then
                ' Console.WriteLine(sLine)
                analizaLinha(sLine)
            End If
        Loop Until sLine Is Nothing
        objReader.Close()
    End Sub

    Sub analizaLinha(sLine As String)
        ' $fileManager->addPHP('widget/widget_entidade.php', $folder);
        sLine = sLine.Trim()
        If (sLine.StartsWith("//")) Then Return

        sLine = sLine.Replace("'", """")
        sLine = sLine.Replace(""" . $folder . """, "")

        If sLine.Contains("addPHP") Then
            Dim indexIni = sLine.IndexOf("addPHP") + 8
            Dim arquivo As String = sLine.Substring(indexIni)
            arquivo = arquivo.Substring(0, arquivo.IndexOf(""""))
            arrPHP.Add(arquivo)

        End If

        If sLine.Contains("addCSS") Then
            Dim indexIni = sLine.IndexOf("addCSS") + 8
            Dim arquivo As String = sLine.Substring(indexIni)
            arquivo = arquivo.Substring(0, arquivo.IndexOf(""""))
            arrCSS.Add(arquivo)

        End If

        ' $fileManager->addJS("js/" . $folder . "/scripts_ajax.js", $folder);

        If sLine.Contains("addJS") Then
            Dim indexIni = sLine.IndexOf("addJS") + 7
            Dim arquivo As String = sLine.Substring(indexIni)
            arquivo = arquivo.Substring(0, arquivo.IndexOf(""""))
            arrJS.Add(arquivo)

        End If
    End Sub

End Class
