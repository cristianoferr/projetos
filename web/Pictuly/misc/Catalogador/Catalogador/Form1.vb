Imports System.IO
Imports System.Text
Imports System.Data.Odbc
Imports System.Drawing
Imports System.Drawing.Drawing2D

Public Class frmCatalogador
    Dim util As util
    Dim local As LocalClient
    Dim workDir As String
    Dim conn As OdbcConnection
    Dim id_media As Integer

    Dim dirDestino As String
    Dim dirDestinoNormal As String
    Dim dirDestinoMini As String
    Dim sizeThresholdKB As Integer = 1000
    Dim widthDestino As Integer = 1920
    Dim heightDestino As Integer = 1080
    Dim pathConvert As String
    Dim mock As String = ""

    Dim flagCopiaArquivo As Boolean = True
    Dim dictTag As New Dictionary(Of String, Integer)


    Private Sub frmCatalogador_Load(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Load


        local = New LocalClient()
        workDir = local.getCurrDir
        util = New util(workDir, "config.ini")
        mock = util.getPar("mock")
        conn = New OdbcConnection(util.getPar("odbcstring" & mock))
        conn.Open()
        util.setConnOdbc(conn)


        pathConvert = util.getPar("path_convert")
        txtFolder.Text = util.getPar("lastdir" & mock)
        widthDestino = util.getPar("width_destino")
        heightDestino = util.getPar("height_destino")
        sizeThresholdKB = util.getPar("size_threshold_kb") * 1024
        txtIdUsuario.Text = util.getPar("id_user")
        txtNiveis.Text = util.getPar("niveis_nome")
        txtUrlOriginal.Text = util.getPar("url_original")
        txtTextoOriginal.Text = util.getPar("texto_original")
        txtPartner.Text = util.getPar("id_partner")
        flagCopiaArquivo = util.getPar("copia_arquivo").Equals("1")
        txtPastasAjeitar.Text = util.getPar("diretoriodestino" & mock)

    End Sub


    Private Sub txtConfig_Leave(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txtConfig.Leave
        salvaConfig()
    End Sub

    Private Sub salvaConfig()
        If (txtConfig.Text.Trim() <> "") Then
            Dim objW As New StreamWriter(workDir & "\config.ini", False)
            Dim sline As String
            For Each sline In txtConfig.Lines
                If (Trim(sline) <> "") Then
                    objW.WriteLine(sline)
                End If
            Next

            objW.Close()
            objW = Nothing


        End If

    End Sub

    Private Sub carregaConfig()
        txtConfig.Text = ""
        Dim obj As StreamReader = New StreamReader(workDir & "\config.ini")
        Dim sline As String
        Do
            sline = obj.ReadLine()
            txtConfig.Text &= sline & Environment.NewLine


        Loop Until sline Is Nothing

        obj.Close()
        obj = Nothing


    End Sub



    Private Sub TabControl_SelectedIndexChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles TabControl.SelectedIndexChanged
        carregaConfig()
    End Sub

    Private Sub btSelecionaFolder_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btSelecionaFolder.Click
        FolderBrowser.SelectedPath = txtFolder.Text
        If FolderBrowser.ShowDialog Then
            txtFolder.Text = FolderBrowser.SelectedPath
        End If

    End Sub
    Sub cataloga()
        txtSaida.Text = ""
        txtLog.Text = ""
        preparaDiretorios()

        If mock = "1" Then
            executaSQL("delete from media_tag")
            executaSQL("delete from media_foto")
            executaSQL("delete from media")
        End If

        executaSQL("select * from usuario ")
        id_media = util.getNext("media", "id_media")
        If id_media = -1 Then
            executaSQL("select * from usuario")
            id_media = util.getNext("media", "id_media")
            If id_media = -1 Then
                executaSQL("select * from usuario")
                id_media = util.getNext("media", "id_media")
                If id_media = -1 Then
                    executaSQL("select * from usuario")
                    id_media = util.getNext("media", "id_media")
                End If
            End If

            If id_media = -1 Then
                MsgBox("Erro de getnext")
                Return
            End If
        End If


        Dim arquivos As FileType() = local.list(txtFolder.Text, False)
        Dim c As Integer = LBound(arquivos)
        While c < UBound(arquivos)
            Dim arquivo As FileType = arquivos(c)
            If (Not arquivo.isFolder) Then
                If (arquivo.nome.ToLower.EndsWith("jpg")) Then
                    adicionaArquivo(arquivo)
                    txtSaida.Text = arquivo.nome & Environment.NewLine & txtSaida.Text
                    Application.DoEvents()
                End If
            End If
            c = c + 1
            If (checkCancel.Checked) Then Return
        End While

        txtSaida.Text = "--Fim" & Environment.NewLine & txtSaida.Text

    End Sub

    Private Sub btCataloga_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btCataloga.Click
        cataloga()
    End Sub

    Sub addLog(ByVal txt As String)
        txtLog.Text = txt & Environment.NewLine & txtLog.Text
        If (txtLog.Text.Length > 10240) Then
            txtLog.Text = txtLog.Text.Substring(0, 10240)
        End If
    End Sub

    Sub preparaDiretorios()
        dirDestino = util.getPar("diretorioDestino" & mock)
        If (Not dirDestino.EndsWith("\")) Then dirDestino &= "\"
        dirDestinoNormal = dirDestino & "normal\"
        dirDestinoMini = dirDestino & "mini\"

        criaPasta(dirDestino)
        criaPasta(dirDestinoNormal)
        criaPasta(dirDestinoMini)

    End Sub

    Sub executaSQL(ByVal sql As String)
        addLog(sql)
        Application.DoEvents()
        Try
            util.executeSQL(sql)
        Catch ex As Exception
            addLog("Erro no ultimo sql: " & ex.Message & " - " & ex.StackTrace)
            If (conn.State = ConnectionState.Closed) Then
                conn.Open()
            End If
        End Try

    End Sub

    Sub adicionaArquivo(ByVal arquivo As FileType)
        Dim flagImproprio As String = "F"
        If checkNSFW.Checked Then flagImproprio = "T"
        Dim nowFunction As String = "dateTime()"
        If (util.getPar("mock") = "") Then
            nowFunction = "now()"
        End If
        Dim camposExtras As String = ""
        Dim valorExtras As String = ""
        If (txtUrlOriginal.Text <> "") Then
            camposExtras &= ",urlOriginal_media, textoOriginal_media"
            valorExtras &= ",'" & txtUrlOriginal.Text & "','" & txtTextoOriginal.Text & "'"
        End If

        If (txtPartner.Text <> "") Then
            camposExtras &= ",id_partner"
            valorExtras &= "," & txtPartner.Text
        End If



        Dim sql As String = "insert into media " & _
        " (id_media,id_usuario_owner,flagimproprio_media,nm_media,id_tipo_media,dtCriacao_media,hitCount_media " & camposExtras & ") values " & _
        " (" & id_media & "," & txtIdUsuario.Text & ",'" & flagImproprio & "','" & arquivo.nome.Substring(0, arquivo.nome.LastIndexOf(".")) & "',1," & nowFunction & ",0 " & valorExtras & ")"

        executaSQL(sql)


        Dim nomeDestino As String = limpa(arquivo.nome, True)
        nomeDestino = nomeDestino.Substring(0, nomeDestino.LastIndexOf(".")) & "." & id_media & nomeDestino.Substring(nomeDestino.LastIndexOf("."))

        Dim dirArquivo As String = getDiretorioArquivo(arquivo.nome)
        If Not redimensiona(arquivo, dirArquivo, nomeDestino) Then
            executaSQL("delete from media_tag where id_media=" & id_media)
            executaSQL("delete from media_foto where id_media=" & id_media)
            executaSQL("delete from media where id_media=" & id_media)
            If flagCopiaArquivo Then
                local.deleteFile(dirDestinoNormal & dirArquivo & nomeDestino)
                local.deleteFile(dirDestinoMini & dirArquivo & nomeDestino)
            End If
            Return
        End If




        tageiaArquivo(arquivo)

        id_media = id_media + 1
    End Sub

    Sub tageiaArquivo(ByVal arquivo As FileType)
        Dim nome = simplifica(arquivo.nome.Substring(0, arquivo.nome.LastIndexOf(".")))
        Dim arrTags As String() = nome.ToString.Split("_")
        Dim sline As String
        For Each sline In arrTags
            If (sline.Length >= 3) Then
                linkaTagMedia(id_media, getTagID(sline))
            End If
        Next


        For Each sline In txtTags.Lines
            If (Trim(sline) <> "") Then
                linkaTagMedia(id_media, getTagID(sline))
            End If
        Next
    End Sub

    Sub linkaTagMedia(ByVal id_media, ByVal id_tag)
        executaSQL("insert into media_tag (id_media,id_tag) values " & _
                       " (" & id_media & "," & id_tag & ")")
    End Sub

    Function simplifica(ByVal texto As String)
        texto = limpa(texto, False)
        texto = texto.Replace("0", "_")
        texto = texto.Replace("'", "_")
        texto = texto.Replace("""", "_")
        texto = texto.Replace("`", "_")
        texto = texto.Replace("~", "_")
        texto = texto.Replace("{", "_")
        texto = texto.Replace("}", "_")
        texto = texto.Replace("[", "_")
        texto = texto.Replace("]", "_")
        texto = texto.Replace("|", "_")
        texto = texto.Replace("+", "_")
        texto = texto.Replace("-", "_")
        texto = texto.Replace("*", "_")
        texto = texto.Replace(";", "_")
        texto = texto.Replace("?", "_")
        texto = texto.Replace("1", "_")
        texto = texto.Replace("2", "_")
        texto = texto.Replace("3", "_")
        texto = texto.Replace("4", "_")
        texto = texto.Replace("5", "_")
        texto = texto.Replace("6", "_")
        texto = texto.Replace("7", "_")
        texto = texto.Replace("8", "_")
        texto = texto.Replace("9", "_")
        texto = texto.Replace("(", "_")
        texto = texto.Replace(")", "_")
        texto = texto.Replace("0", "_")
        texto = texto.Replace("__", "_")
        texto = texto.Replace("__", "_")
        Return texto

    End Function

    Function getDiretorioArquivo(ByVal arquivoName As String) As String
        arquivoName = arquivoName.Substring(0, arquivoName.LastIndexOf("."))
        arquivoName = limpa(arquivoName, False)
        Dim niveis As Integer = txtNiveis.Text
        Dim c As Integer = 0
        Dim dir As String = ""
        While ((c < niveis) And (c < arquivoName.Length))
            dir &= arquivoName(c) & "\"
            dir = dir.ToLower

            criaPasta(dirDestinoMini & dir)
            criaPasta(dirDestinoNormal & dir)
            c = c + 1
        End While
        Return dir
    End Function

    Function limpa(ByVal nome As String, ByVal flagContemExtensao As Boolean)
        Dim ext As String = ""
        If flagContemExtensao Then
            ext = nome.Substring(nome.LastIndexOf("."))
            nome = nome.Substring(0, nome.LastIndexOf("."))
        End If
        nome = nome.Replace(" ", "_")
        nome = nome.Replace(".", "_")
        nome = nome.Replace("-", "_")
        nome = nome.Replace("+", "_")
        nome = nome.Replace("#", "_")
        nome = nome.Replace("%", "_")
        nome = nome.Replace("&", "_")
        nome = nome.Replace("?", "_")
        nome = nome.Replace("!", "_")
        nome = nome.Replace("(", "_")
        nome = nome.Replace(")", "_")
        nome = nome.Replace("@", "_")
        nome = nome.Replace("$", "_")
        nome = util.RemoveAcentos(nome)
        Return nome & ext
    End Function

    Sub criaPasta(ByVal pasta As String)
        Try
            local.createFolder2(pasta)
        Catch ex As Exception

        End Try
    End Sub

    Function redimensiona(ByVal arquivo As FileType, ByVal dirArquivo As String, ByVal nomeDestino As String) As Boolean
        Try


            Dim original As Image = Image.FromFile(arquivo.path)
            Dim tags As String = util.ReadFileAttributes(arquivo.path)

            Dim destinoArquivoNormal As String = dirDestinoNormal & dirArquivo & nomeDestino


            Dim imageSize As Size
            Dim flagcopiaLocal As Boolean = True

            If flagCopiaArquivo And flagcopiaLocal Then
                If (arquivo.tamanho > sizeThresholdKB) Then
                    imageSize = ImageUtils.resizeSaveCmd(util, pathConvert, original, arquivo, New Size(widthDestino, heightDestino), destinoArquivoNormal)
                Else
                    local.copyFile(arquivo.path, destinoArquivoNormal)
                    imageSize = New Size(original.Width, original.Height)
                End If
            Else
                imageSize = ImageUtils.getRatioSize(original, New Size(widthDestino, heightDestino))
            End If



            Dim dirArquivoWeb As String = dirArquivo.Replace("\", "/")
            executaSQL("insert into media_foto (id_media,widthPicture_media,heightPicture_media,filename_media) values " & _
            " (" & id_media & "," & imageSize.Width & "," & imageSize.Height & ",'" & dirArquivoWeb & nomeDestino & "')")

            If (original.Width + original.Height) > 1500 Then
                linkaTagMedia(id_media, getTagID("hi res"))
            ElseIf (original.Width + original.Height) < 500 Then
                linkaTagMedia(id_media, getTagID("low res"))
            Else
                linkaTagMedia(id_media, getTagID("std res"))
            End If

            linkaTags(tags, id_media)

            Dim miniWidth As Integer = util.getPar("miniWidth")
            Dim miniHeight As Integer = util.getPar("miniHeight")
            If flagCopiaArquivo Then
                Dim resized As Image = ImageUtils.ResizeImage(original, New Size(miniWidth, miniHeight))

                resized.Save(dirDestinoMini & dirArquivo & nomeDestino, System.Drawing.Imaging.ImageFormat.Jpeg)
            End If


        Catch ex As Exception
            Return False
        End Try

        Return True

    End Function

    Sub linkaTags(ByVal tags As String, ByVal id_media As String)
        Dim arr = tags.Split(";")
        Dim sline As String
        For Each sline In arr
            linkaTagMedia(id_media, getTagID(sline))
        Next

    End Sub

    Function getTagID(ByVal tagName As String) As String
        tagName = tagName.ToLower.Trim
        If (dictTag.ContainsKey(tagName)) Then
            Return dictTag.Item(tagName)
        End If

        Dim id_tag As String = util.getValorOdbc("select id_tag from tag where nm_tag='" & tagName & "'", "id_tag")
        If id_tag Is Nothing Then
            id_tag = util.getNext("tag", "id_tag")
            executaSQL("insert into tag (id_tag,nm_tag) values (" & id_tag & ",'" & tagName & "')")
        End If

        dictTag.Add(tagName, id_tag)
        Return id_tag
    End Function


    Private Sub txtFolder_Validated(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txtFolder.Validated
        util.setPar("lastdir" & mock, txtFolder.Text)
    End Sub

    Private Sub txtIdUsuario_Validated(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txtIdUsuario.Validated
        util.setPar("id_user", txtIdUsuario.Text)
    End Sub

    Private Sub txtNiveis_Validated(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txtNiveis.Validated
        util.setPar("niveis_nome", txtNiveis.Text)
    End Sub

    Private Sub txtUrlOriginal_Validated(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txtUrlOriginal.Validated
        util.setPar("url_original", txtUrlOriginal.Text)
    End Sub

    Private Sub txtTextoOriginal_TextChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txtTextoOriginal.TextChanged
        util.setPar("texto_original", txtTextoOriginal.Text)
    End Sub

    Private Sub txtPartner_Validated(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txtPartner.Validated
        util.setPar("id_partner", txtPartner.Text)
    End Sub

    Private Sub btCatalogaPasta_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btCatalogaPasta.Click
        Dim sline As String
        For Each sline In txtPastas.Lines
            If (Trim(sline) <> "") Then
                Dim arrPasta As String() = sline.Split(";")
                txtFolder.Text = arrPasta(0)
                txtUrlOriginal.Text = arrPasta(1)
                txtTextoOriginal.Text = arrPasta(2)
                checkNSFW.Checked = arrPasta(3).ToString.Equals("t")
                Dim c As Integer = 4
                txtTags.Text = ""
                While c < arrPasta.Length
                    txtTags.Text &= arrPasta(c) & Environment.NewLine
                    c = c + 1
                End While
                TabControl.SelectedIndex = 1
                cataloga()
                If (checkCancel.Checked) Then Return

                conn.Close()
                conn.Open()
            End If
        Next
    End Sub

    Private Sub btAjeitador_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btAjeitador.Click
        Dim pastaRaiz As String = txtPastasAjeitar.Text & "\normal\"
        Dim arquivos As FileType() = local.list(pastaRaiz, True)
        Dim arquivo As FileType
        For Each arquivo In arquivos
            If Not arquivo.isFolder Then

                txtAjeitaLog.Text = arquivo.path & Environment.NewLine & txtAjeitaLog.Text

                If (txtAjeitaLog.Text.Length > 10240) Then
                    txtAjeitaLog.Text = txtAjeitaLog.Text.Substring(0, 10240)
                End If
                Dim path As String = arquivo.path.Substring(pastaRaiz.Length)

                Dim nome As String = arquivo.nome
                nome = nome.Substring(0, nome.LastIndexOf("."))

                Dim id As String = nome.Substring(nome.LastIndexOf(".") + 1)
                nome = nome.Substring(0, nome.LastIndexOf("."))
                path = path.Replace("\", "/")

                executaSQL("update media set nm_media='" & nome & "' where id_media=" & id)
                executaSQL("update media_foto set filename_media='" & path & "' where id_media=" & id)

            End If
        Next

    End Sub

    Private Sub btRetageia_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles btRetageia.Click

        Dim pastaRaiz As String = txtPastasAjeitar.Text & "\normal\"
        Dim arquivos As FileType() = local.list(pastaRaiz, True)
        Dim arquivo As FileType
        For Each arquivo In arquivos
            If Not arquivo.isFolder Then
                txtAjeitaLog.Text = arquivo.path & Environment.NewLine & txtAjeitaLog.Text
                If (txtAjeitaLog.Text.Length > 10240) Then
                    txtAjeitaLog.Text = txtAjeitaLog.Text.Substring(0, 10240)
                End If


            End If
        Next
    End Sub

    Private Sub BtAfiliado1_Click(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles BtAfiliado1.Click
        Dim sline As String
        checkNSFW.Checked = True
        txtPartner.Text = 2
        Dim count As Integer = 1
        Dim recomeca As Integer = 340
        For Each sline In txtGalerias1.Lines
            If (sline.Length >= 3) Then
                If count >= recomeca Then
                    catalogaAfiliado(count, sline)
                End If
                count = count + 1
            End If
        Next
    End Sub

    Sub catalogaAfiliado(ByVal galeria As Integer, ByVal sline As String)
        'url|descr|title|modele
        Dim pasta As String = txtPastaAfiliado1.Text & "\" & galeria & "\"
        txtFolder.Text = pasta
        Dim arrParams As String() = sline.ToString.Split("|")
        Dim link As String = arrParams(0)
        Dim rootLink As String = link.Replace("http://", "")
        rootLink = "http://" & rootLink.Substring(0, rootLink.IndexOf("/"))

        txtUrlOriginal.Text = link

        Dim arrTags = arrParams(1).ToLower & " " & arrParams(3).ToString.ToLower & " " & arrParams(2).ToString.ToLower
        arrTags = arrTags.ToString.Replace("no panties", "no_panties")
        arrTags = arrTags.ToString.Replace("big tits", "big_tits")
        arrTags = arrTags.ToString.Replace("big tities", "big_tits")
        arrTags = arrTags.Split(" ")
        Dim titulo = arrParams(3) & " - " & arrParams(2)
        txtTextoOriginal.Text = titulo
        '  local.deleteFolder(pasta)
        local.createFolder2(pasta)
        txtTags.Text = ""
        Dim tag As String
        txtTags.Text &= "all" & Environment.NewLine
        txtTags.Text &= "picture" & Environment.NewLine
        txtTags.Text &= "nsfw" & Environment.NewLine
        For Each tag In arrTags
            txtTags.Text &= tag & Environment.NewLine
        Next

        Dim textoHtml As String = util.retrieveWebPage(link)

        While textoHtml.Contains(".jpg'")
            textoHtml = textoHtml.Substring(textoHtml.IndexOf("<a href") + 9)
            Dim imagem As String = textoHtml.Substring(0, textoHtml.IndexOf(".jpg'>") + 4)
            textoHtml = textoHtml.Replace(imagem, "")
            Dim imageLocal As String = imagem.Substring(imagem.LastIndexOf("/") + 1)
            If (imageLocal.Contains(".jpg")) Then
                Console.WriteLine(rootLink & "+" & imagem & " -> " & imageLocal)
                util.saveWebPage(rootLink & imagem, pasta & imageLocal)
            End If
            Application.DoEvents()

        End While

        TabControl.SelectedIndex = 1
        cataloga()

        TabControl.SelectedIndex = 4


    End Sub

End Class
