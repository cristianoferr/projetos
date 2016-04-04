Public Class BluePrintController
    Public blueprint As Blueprint = Nothing
    Dim paramLists As ArrayList = New ArrayList()
    Dim vFrmPrincipal As FrmPrincipal

    Dim posX As Integer = 0
    Dim posY As Integer = 0

    Public Sub New(ByVal FrmPrincipal As FrmPrincipal)
        InitializeComponent()
        vFrmPrincipal = FrmPrincipal
    End Sub

    Dim syntax As BPSyntax = New BPSyntax


    Private Sub BluePrintController_Load(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Load

    End Sub

    Public Shared Function unificaNewLine(texto As String) As String
        texto = texto.Replace(vbCr, vbNewLine)
        texto = texto.Replace(vbLf, vbNewLine)

        texto = texto.Replace(vbCr & vbCr, vbCr)
        texto = texto.Replace(vbNewLine & vbNewLine, vbNewLine)
        Return texto
    End Function

    Public Sub atualizaBlueprintFromCode(bp As Blueprint, texto As String, frm As FrmPrincipal)
        parseBlueprintIntoBP(bp, texto, frm)
    End Sub

    Public Shared Function parseBlueprint(texto As String, frm As FrmPrincipal) As Blueprint
        Dim bp As Blueprint = New Blueprint("", "", "")
        Return parseBlueprintIntoBP(bp, texto, frm)
    End Function

    Public Shared Function parseBlueprintIntoBP(bp As Blueprint, texto As String, frm As FrmPrincipal) As Blueprint
        texto = unificaNewLine(texto)
        texto = texto & vbNewLine
        Dim header As String = texto.Substring(0, texto.IndexOf(vbNewLine))
        Dim headerOrig As String = header
        Dim coment As String = ""
       
        If (header.Contains("//")) Then
            coment = header.Substring(header.IndexOf("//") + 2)
            header = header.Substring(0, header.IndexOf("//")).Trim
        End If
        Dim tipo As String = header.Substring(1, header.IndexOf(" ")).Trim
        Dim nome As String = header.Substring(header.IndexOf(" ")).Trim
        Dim extends As String = ""
        If (nome.Contains(":")) Then
            extends = nome.Substring(nome.IndexOf(":") + 1)
            nome = nome.Substring(0, nome.IndexOf(":"))
        Else
            If nome <> frm.baseElement Then
                Console.WriteLine(nome & " não extende ninguém!")
            End If
        End If
        texto = texto.Replace(headerOrig & vbNewLine, "")
        ' Dim bp As Blueprint = New Blueprint(nome, extends, tipo)
        bp.nome = nome
        bp.extends = extends
        bp.tipo = tipo
        bp.coment = coment
        bp.clearLists()

        While texto.Length > 0
            texto = texto.Trim
            Dim linha As String = ""
            Try

            
                linha = texto.Substring(0, texto.IndexOf("@", 1) - 1).Trim
            Catch ex As Exception
                MsgBox("Erro na linha: " & texto & " ex:" & ex.Message)
            End Try
            texto = texto.Replace(linha, "").Trim
            If linha = "@end" Then
                linha = ""
            End If

            If linha.IndexOf(" ") > 0 And (linha <> "") Then
                Dim lista As String = linha.Substring(1, linha.IndexOf(" ") - 1).Trim
                Dim ident As String = linha.Substring(lista.Length + 2)
                Dim modifier As String = "="
                Dim param As String
                If (ident.Contains("+=")) Then modifier = "+="
                If (ident.Contains("-=")) Then modifier = "-="
                If (ident.Contains("*=")) Then modifier = "*="
                If (ident.Contains("/=")) Then modifier = "/="
                param = ident.Substring(ident.IndexOf(modifier) + modifier.Length)
                Try

                    ident = ident.Substring(0, ident.IndexOf(modifier))
                Catch ex As Exception
                    MsgBox("Erro em " & headerOrig & " > " & ident)

                End Try
                coment = ""
                Dim comentAtual As String = ""
                If (param.Contains("//*")) Then
                    coment = param.Substring(param.IndexOf("//") + 3)
                    param = param.Substring(0, param.IndexOf("//")).Trim
                    comentAtual = bp.getValue("comment", "description")
                    If (comentAtual <> "") Then
                        coment = comentAtual & "#$#%" & coment
                    End If
                    bp.setParam("comment", "description", "=", """" & coment & """", "")
                    coment = ""
                End If

                If (param.Contains("//TODO:")) Then
                    adicionaTodo(nome & ": " & param.Substring(param.IndexOf("//TODO:") + 7), frm)
                End If

                If (param.Contains("//")) Then
                    coment = param.Substring(param.IndexOf("//") + 2)
                    param = param.Substring(0, param.IndexOf("//")).Trim
                End If

                comentAtual = bp.getValue("comment", "description")
                If (lista = "comment") And ident = "description" And (comentAtual <> "") Then
                    param = """" & param.Replace("""", "") & "#$#%" & comentAtual.Replace("""", "") & """"
                End If

                bp.setParam(lista, ident, modifier, param, coment)

            End If
            If (texto = "@end") Then
                texto = ""
            End If
        End While

        Return bp

    End Function


    Public Shared Sub adicionaTodo(sline As String, frm As FrmPrincipal)
        frm.addTodo(sline)
    End Sub
    Public Sub loadFrom(bp As Blueprint)
        blueprint = bp
        FrmPrincipal.txtCode.Text = unificaNewLine(bp.generate)
        FrmPrincipal.txtCode.Tag = FrmPrincipal.txtCode.Text

        syntax.highLightText(FrmPrincipal.txtCode)


        FrmPrincipal.txtComment.Text = bp.getValueAsText("comment", "description")
        FrmPrincipal.txtComment.Tag = FrmPrincipal.txtComment.Text
        updateFromModel()
    End Sub

    Public Sub startWith(ByVal texto As String)
        blueprint = parseBlueprint(texto, vFrmPrincipal)
        FrmPrincipal.txtCode.Text = unificaNewLine(texto)
        updateFromModel()
    End Sub

    Sub updateFromModel()
        cmbType.Text = blueprint.tipo
        txtName.Text = blueprint.nome
        cmbExtends.Text = blueprint.extends

        panelConteudo.Controls.Clear()
        paramLists.Clear()
        Dim par As ParamList
        For Each par In blueprint.listas
            If par.listName <> "comment" Then
                Console.WriteLine("Criando ParamListController para " & par.listName)
                Dim paramList As ParamListController = New ParamListController(par, Me, blueprint)
                panelConteudo.Controls.Add(paramList)
                paramLists.Add(paramList)
                paramList.Dock = DockStyle.Top
            End If
        Next

        updateSize()
    End Sub
    Public Sub updateSize()
        'updateWidth()
        updateHeight()
    End Sub
    Public Sub updateWidth()
        Dim minWidth As Integer = cmbExtends.Left + cmbExtends.Width
        Dim par As ParamListController
        For Each par In paramLists
            Dim w As Integer = par.idealWidth
            If (w > minWidth) Then minWidth = w
        Next
        Width = minWidth
    End Sub

    Public Sub updateHeight()
        Height = 18
        Dim par As ParamListController
        For Each par In paramLists
            Height = Height + par.idealHeight
        Next
    End Sub


    Private Sub Panel1_Move(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles Panel1.Move
        Console.WriteLine("panel1.move")
    End Sub

    Private Sub Panel1_MouseDown(ByVal sender As System.Object, ByVal e As System.Windows.Forms.MouseEventArgs) Handles Panel1.MouseDown
        posX = e.Location.X
        posY = e.Location.Y
    End Sub

    Private Sub Panel1_MouseUp(ByVal sender As System.Object, ByVal e As System.Windows.Forms.MouseEventArgs) Handles Panel1.MouseUp
        posX = 0
        posY = 0
    End Sub

    Private Sub Panel1_MouseMove(ByVal sender As System.Object, ByVal e As System.Windows.Forms.MouseEventArgs) Handles Panel1.MouseMove
        If posX > 0 Then
            Dim difX = e.Location.X - posX
            Dim difY = e.Location.Y - posY
            Left = Left + difX
            Top = Top + difY
            Console.WriteLine("blueprint mouse move: " & e.Location.X & " " & e.Location.Y)
        End If


    End Sub


    Private Sub linkNovaLista_LinkClicked(sender As System.Object, e As System.Windows.Forms.LinkLabelLinkClickedEventArgs) Handles linkNovaLista.LinkClicked
        blueprint.getListaFor(InputBox("Nome da Lista", "Lista Nova"))
        updateFromModel()
    End Sub

    Private Sub cmbType_TextChanged(sender As System.Object, e As System.EventArgs) Handles cmbType.TextChanged
        If blueprint Is Nothing Then Return
        If blueprint.tipo = cmbType.Text Then Return
        blueprint.tipo = cmbType.Text

        '    FrmPrincipal.atualiza()
        FrmPrincipal.selecionaNode(txtName.Text, True)
    End Sub


    Private Sub txtName_Validated(sender As System.Object, e As System.EventArgs) Handles txtName.Validated
        If blueprint Is Nothing Then Return
        If blueprint.nome = txtName.Text Then Return
        blueprint.nome = txtName.Text
        FrmPrincipal.atualiza()
        FrmPrincipal.selecionaNode(txtName.Text, True)
    End Sub

    Private Sub linkExtends_LinkClicked(sender As System.Object, e As System.Windows.Forms.LinkLabelLinkClickedEventArgs)
        FrmPrincipal.selecionaNode(cmbExtends.Text, True)
    End Sub

    Private Sub cmbExtends_SelectedIndexChanged(sender As System.Object, e As System.EventArgs) Handles cmbExtends.SelectedIndexChanged

    End Sub
End Class
