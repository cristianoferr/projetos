<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class frmCatalogador
    Inherits System.Windows.Forms.Form

    'Form overrides dispose to clean up the component list.
    <System.Diagnostics.DebuggerNonUserCode()> _
    Protected Overrides Sub Dispose(ByVal disposing As Boolean)
        Try
            If disposing AndAlso components IsNot Nothing Then
                components.Dispose()
            End If
        Finally
            MyBase.Dispose(disposing)
        End Try
    End Sub

    'Required by the Windows Form Designer
    Private components As System.ComponentModel.IContainer

    'NOTE: The following procedure is required by the Windows Form Designer
    'It can be modified using the Windows Form Designer.  
    'Do not modify it using the code editor.
    <System.Diagnostics.DebuggerStepThrough()> _
    Private Sub InitializeComponent()
        Dim resources As System.ComponentModel.ComponentResourceManager = New System.ComponentModel.ComponentResourceManager(GetType(frmCatalogador))
        Me.TabControl = New System.Windows.Forms.TabControl
        Me.tabPastas = New System.Windows.Forms.TabPage
        Me.btCatalogaPasta = New System.Windows.Forms.Button
        Me.txtPastas = New System.Windows.Forms.TextBox
        Me.TabPage1 = New System.Windows.Forms.TabPage
        Me.Label7 = New System.Windows.Forms.Label
        Me.txtPartner = New System.Windows.Forms.TextBox
        Me.Label6 = New System.Windows.Forms.Label
        Me.txtTextoOriginal = New System.Windows.Forms.TextBox
        Me.Label5 = New System.Windows.Forms.Label
        Me.txtUrlOriginal = New System.Windows.Forms.TextBox
        Me.checkCancel = New System.Windows.Forms.CheckBox
        Me.txtLog = New System.Windows.Forms.TextBox
        Me.Label4 = New System.Windows.Forms.Label
        Me.txtNiveis = New System.Windows.Forms.TextBox
        Me.Label3 = New System.Windows.Forms.Label
        Me.txtIdUsuario = New System.Windows.Forms.TextBox
        Me.checkNSFW = New System.Windows.Forms.CheckBox
        Me.txtSaida = New System.Windows.Forms.TextBox
        Me.btCataloga = New System.Windows.Forms.Button
        Me.Label2 = New System.Windows.Forms.Label
        Me.txtTags = New System.Windows.Forms.TextBox
        Me.btSelecionaFolder = New System.Windows.Forms.Button
        Me.Label1 = New System.Windows.Forms.Label
        Me.txtFolder = New System.Windows.Forms.TextBox
        Me.TabPage2 = New System.Windows.Forms.TabPage
        Me.txtConfig = New System.Windows.Forms.TextBox
        Me.TabAjeitador = New System.Windows.Forms.TabPage
        Me.btRetageia = New System.Windows.Forms.Button
        Me.txtAjeitaLog = New System.Windows.Forms.TextBox
        Me.btAjeitador = New System.Windows.Forms.Button
        Me.Label8 = New System.Windows.Forms.Label
        Me.txtPastasAjeitar = New System.Windows.Forms.TextBox
        Me.TabPage3 = New System.Windows.Forms.TabPage
        Me.Label9 = New System.Windows.Forms.Label
        Me.txtPastaAfiliado1 = New System.Windows.Forms.TextBox
        Me.BtAfiliado1 = New System.Windows.Forms.Button
        Me.txtGalerias1 = New System.Windows.Forms.TextBox
        Me.FolderBrowser = New System.Windows.Forms.FolderBrowserDialog
        Me.TextBox2 = New System.Windows.Forms.TextBox
        Me.TabControl.SuspendLayout()
        Me.tabPastas.SuspendLayout()
        Me.TabPage1.SuspendLayout()
        Me.TabPage2.SuspendLayout()
        Me.TabAjeitador.SuspendLayout()
        Me.TabPage3.SuspendLayout()
        Me.SuspendLayout()
        '
        'TabControl
        '
        Me.TabControl.Controls.Add(Me.tabPastas)
        Me.TabControl.Controls.Add(Me.TabPage1)
        Me.TabControl.Controls.Add(Me.TabPage2)
        Me.TabControl.Controls.Add(Me.TabAjeitador)
        Me.TabControl.Controls.Add(Me.TabPage3)
        Me.TabControl.Dock = System.Windows.Forms.DockStyle.Fill
        Me.TabControl.Location = New System.Drawing.Point(0, 0)
        Me.TabControl.Name = "TabControl"
        Me.TabControl.SelectedIndex = 0
        Me.TabControl.Size = New System.Drawing.Size(614, 463)
        Me.TabControl.TabIndex = 0
        '
        'tabPastas
        '
        Me.tabPastas.Controls.Add(Me.btCatalogaPasta)
        Me.tabPastas.Controls.Add(Me.txtPastas)
        Me.tabPastas.Location = New System.Drawing.Point(4, 22)
        Me.tabPastas.Name = "tabPastas"
        Me.tabPastas.Size = New System.Drawing.Size(606, 437)
        Me.tabPastas.TabIndex = 2
        Me.tabPastas.Text = "Pastas"
        Me.tabPastas.UseVisualStyleBackColor = True
        '
        'btCatalogaPasta
        '
        Me.btCatalogaPasta.Location = New System.Drawing.Point(3, 317)
        Me.btCatalogaPasta.Name = "btCatalogaPasta"
        Me.btCatalogaPasta.Size = New System.Drawing.Size(75, 24)
        Me.btCatalogaPasta.TabIndex = 6
        Me.btCatalogaPasta.Text = "Catalogar"
        Me.btCatalogaPasta.UseVisualStyleBackColor = True
        '
        'txtPastas
        '
        Me.txtPastas.Dock = System.Windows.Forms.DockStyle.Top
        Me.txtPastas.Location = New System.Drawing.Point(0, 0)
        Me.txtPastas.Multiline = True
        Me.txtPastas.Name = "txtPastas"
        Me.txtPastas.Size = New System.Drawing.Size(606, 301)
        Me.txtPastas.TabIndex = 4
        '
        'TabPage1
        '
        Me.TabPage1.Controls.Add(Me.Label7)
        Me.TabPage1.Controls.Add(Me.txtPartner)
        Me.TabPage1.Controls.Add(Me.Label6)
        Me.TabPage1.Controls.Add(Me.txtTextoOriginal)
        Me.TabPage1.Controls.Add(Me.Label5)
        Me.TabPage1.Controls.Add(Me.txtUrlOriginal)
        Me.TabPage1.Controls.Add(Me.checkCancel)
        Me.TabPage1.Controls.Add(Me.txtLog)
        Me.TabPage1.Controls.Add(Me.Label4)
        Me.TabPage1.Controls.Add(Me.txtNiveis)
        Me.TabPage1.Controls.Add(Me.Label3)
        Me.TabPage1.Controls.Add(Me.txtIdUsuario)
        Me.TabPage1.Controls.Add(Me.checkNSFW)
        Me.TabPage1.Controls.Add(Me.txtSaida)
        Me.TabPage1.Controls.Add(Me.btCataloga)
        Me.TabPage1.Controls.Add(Me.Label2)
        Me.TabPage1.Controls.Add(Me.txtTags)
        Me.TabPage1.Controls.Add(Me.btSelecionaFolder)
        Me.TabPage1.Controls.Add(Me.Label1)
        Me.TabPage1.Controls.Add(Me.txtFolder)
        Me.TabPage1.Location = New System.Drawing.Point(4, 22)
        Me.TabPage1.Name = "TabPage1"
        Me.TabPage1.Padding = New System.Windows.Forms.Padding(3)
        Me.TabPage1.Size = New System.Drawing.Size(606, 437)
        Me.TabPage1.TabIndex = 0
        Me.TabPage1.Text = "Catalogador"
        Me.TabPage1.UseVisualStyleBackColor = True
        '
        'Label7
        '
        Me.Label7.AutoSize = True
        Me.Label7.Location = New System.Drawing.Point(325, 82)
        Me.Label7.Name = "Label7"
        Me.Label7.Size = New System.Drawing.Size(55, 13)
        Me.Label7.TabIndex = 20
        Me.Label7.Text = "ID Partner"
        '
        'txtPartner
        '
        Me.txtPartner.Location = New System.Drawing.Point(387, 79)
        Me.txtPartner.Name = "txtPartner"
        Me.txtPartner.Size = New System.Drawing.Size(40, 20)
        Me.txtPartner.TabIndex = 19
        '
        'Label6
        '
        Me.Label6.AutoSize = True
        Me.Label6.Location = New System.Drawing.Point(6, 178)
        Me.Label6.Name = "Label6"
        Me.Label6.Size = New System.Drawing.Size(72, 13)
        Me.Label6.TabIndex = 18
        Me.Label6.Text = "Texto Original"
        '
        'txtTextoOriginal
        '
        Me.txtTextoOriginal.Location = New System.Drawing.Point(84, 175)
        Me.txtTextoOriginal.Name = "txtTextoOriginal"
        Me.txtTextoOriginal.Size = New System.Drawing.Size(230, 20)
        Me.txtTextoOriginal.TabIndex = 17
        '
        'Label5
        '
        Me.Label5.AutoSize = True
        Me.Label5.Location = New System.Drawing.Point(8, 148)
        Me.Label5.Name = "Label5"
        Me.Label5.Size = New System.Drawing.Size(58, 13)
        Me.Label5.TabIndex = 16
        Me.Label5.Text = "Url Original"
        '
        'txtUrlOriginal
        '
        Me.txtUrlOriginal.Location = New System.Drawing.Point(84, 145)
        Me.txtUrlOriginal.Name = "txtUrlOriginal"
        Me.txtUrlOriginal.Size = New System.Drawing.Size(230, 20)
        Me.txtUrlOriginal.TabIndex = 15
        '
        'checkCancel
        '
        Me.checkCancel.AutoSize = True
        Me.checkCancel.Location = New System.Drawing.Point(368, 23)
        Me.checkCancel.Name = "checkCancel"
        Me.checkCancel.Size = New System.Drawing.Size(59, 17)
        Me.checkCancel.TabIndex = 14
        Me.checkCancel.Text = "Cancel"
        Me.checkCancel.UseVisualStyleBackColor = True
        '
        'txtLog
        '
        Me.txtLog.Dock = System.Windows.Forms.DockStyle.Bottom
        Me.txtLog.Location = New System.Drawing.Point(3, 201)
        Me.txtLog.Multiline = True
        Me.txtLog.Name = "txtLog"
        Me.txtLog.ScrollBars = System.Windows.Forms.ScrollBars.Vertical
        Me.txtLog.Size = New System.Drawing.Size(430, 233)
        Me.txtLog.TabIndex = 13
        '
        'Label4
        '
        Me.Label4.AutoSize = True
        Me.Label4.Location = New System.Drawing.Point(326, 119)
        Me.Label4.Name = "Label4"
        Me.Label4.Size = New System.Drawing.Size(36, 13)
        Me.Label4.TabIndex = 12
        Me.Label4.Text = "Niveis"
        '
        'txtNiveis
        '
        Me.txtNiveis.Location = New System.Drawing.Point(368, 116)
        Me.txtNiveis.Name = "txtNiveis"
        Me.txtNiveis.Size = New System.Drawing.Size(40, 20)
        Me.txtNiveis.TabIndex = 11
        '
        'Label3
        '
        Me.Label3.AutoSize = True
        Me.Label3.Location = New System.Drawing.Point(325, 59)
        Me.Label3.Name = "Label3"
        Me.Label3.Size = New System.Drawing.Size(57, 13)
        Me.Label3.TabIndex = 10
        Me.Label3.Text = "ID Usuario"
        '
        'txtIdUsuario
        '
        Me.txtIdUsuario.Location = New System.Drawing.Point(387, 56)
        Me.txtIdUsuario.Name = "txtIdUsuario"
        Me.txtIdUsuario.Size = New System.Drawing.Size(40, 20)
        Me.txtIdUsuario.TabIndex = 9
        '
        'checkNSFW
        '
        Me.checkNSFW.AutoSize = True
        Me.checkNSFW.Location = New System.Drawing.Point(329, 39)
        Me.checkNSFW.Name = "checkNSFW"
        Me.checkNSFW.Size = New System.Drawing.Size(58, 17)
        Me.checkNSFW.TabIndex = 8
        Me.checkNSFW.Text = "NSFW"
        Me.checkNSFW.UseVisualStyleBackColor = True
        '
        'txtSaida
        '
        Me.txtSaida.Dock = System.Windows.Forms.DockStyle.Right
        Me.txtSaida.Location = New System.Drawing.Point(433, 3)
        Me.txtSaida.Multiline = True
        Me.txtSaida.Name = "txtSaida"
        Me.txtSaida.Size = New System.Drawing.Size(170, 431)
        Me.txtSaida.TabIndex = 6
        '
        'btCataloga
        '
        Me.btCataloga.Location = New System.Drawing.Point(329, 142)
        Me.btCataloga.Name = "btCataloga"
        Me.btCataloga.Size = New System.Drawing.Size(75, 24)
        Me.btCataloga.TabIndex = 5
        Me.btCataloga.Text = "Catalogar"
        Me.btCataloga.UseVisualStyleBackColor = True
        '
        'Label2
        '
        Me.Label2.AutoSize = True
        Me.Label2.Location = New System.Drawing.Point(5, 40)
        Me.Label2.Name = "Label2"
        Me.Label2.Size = New System.Drawing.Size(31, 13)
        Me.Label2.TabIndex = 4
        Me.Label2.Text = "Tags"
        '
        'txtTags
        '
        Me.txtTags.Location = New System.Drawing.Point(7, 56)
        Me.txtTags.Multiline = True
        Me.txtTags.Name = "txtTags"
        Me.txtTags.Size = New System.Drawing.Size(204, 80)
        Me.txtTags.TabIndex = 3
        Me.txtTags.Text = "all" & Global.Microsoft.VisualBasic.ChrW(13) & Global.Microsoft.VisualBasic.ChrW(10) & "image"
        '
        'btSelecionaFolder
        '
        Me.btSelecionaFolder.Location = New System.Drawing.Point(244, 20)
        Me.btSelecionaFolder.Name = "btSelecionaFolder"
        Me.btSelecionaFolder.Size = New System.Drawing.Size(75, 19)
        Me.btSelecionaFolder.TabIndex = 2
        Me.btSelecionaFolder.Text = "Abrir"
        Me.btSelecionaFolder.UseVisualStyleBackColor = True
        '
        'Label1
        '
        Me.Label1.AutoSize = True
        Me.Label1.Location = New System.Drawing.Point(7, 4)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(78, 13)
        Me.Label1.TabIndex = 1
        Me.Label1.Text = "Pasta Arquivos"
        '
        'txtFolder
        '
        Me.txtFolder.Location = New System.Drawing.Point(6, 20)
        Me.txtFolder.Name = "txtFolder"
        Me.txtFolder.Size = New System.Drawing.Size(231, 20)
        Me.txtFolder.TabIndex = 0
        '
        'TabPage2
        '
        Me.TabPage2.Controls.Add(Me.txtConfig)
        Me.TabPage2.Location = New System.Drawing.Point(4, 22)
        Me.TabPage2.Name = "TabPage2"
        Me.TabPage2.Padding = New System.Windows.Forms.Padding(3)
        Me.TabPage2.Size = New System.Drawing.Size(606, 437)
        Me.TabPage2.TabIndex = 1
        Me.TabPage2.Text = "Config"
        Me.TabPage2.UseVisualStyleBackColor = True
        '
        'txtConfig
        '
        Me.txtConfig.Dock = System.Windows.Forms.DockStyle.Fill
        Me.txtConfig.Location = New System.Drawing.Point(3, 3)
        Me.txtConfig.Multiline = True
        Me.txtConfig.Name = "txtConfig"
        Me.txtConfig.Size = New System.Drawing.Size(600, 431)
        Me.txtConfig.TabIndex = 0
        '
        'TabAjeitador
        '
        Me.TabAjeitador.Controls.Add(Me.btRetageia)
        Me.TabAjeitador.Controls.Add(Me.txtAjeitaLog)
        Me.TabAjeitador.Controls.Add(Me.btAjeitador)
        Me.TabAjeitador.Controls.Add(Me.Label8)
        Me.TabAjeitador.Controls.Add(Me.txtPastasAjeitar)
        Me.TabAjeitador.Location = New System.Drawing.Point(4, 22)
        Me.TabAjeitador.Name = "TabAjeitador"
        Me.TabAjeitador.Size = New System.Drawing.Size(606, 437)
        Me.TabAjeitador.TabIndex = 3
        Me.TabAjeitador.Text = "Ajeitador"
        Me.TabAjeitador.UseVisualStyleBackColor = True
        '
        'btRetageia
        '
        Me.btRetageia.Location = New System.Drawing.Point(453, 16)
        Me.btRetageia.Name = "btRetageia"
        Me.btRetageia.Size = New System.Drawing.Size(75, 19)
        Me.btRetageia.TabIndex = 15
        Me.btRetageia.Text = "Retageia"
        Me.btRetageia.UseVisualStyleBackColor = True
        '
        'txtAjeitaLog
        '
        Me.txtAjeitaLog.BackColor = System.Drawing.Color.WhiteSmoke
        Me.txtAjeitaLog.Dock = System.Windows.Forms.DockStyle.Bottom
        Me.txtAjeitaLog.Location = New System.Drawing.Point(0, 128)
        Me.txtAjeitaLog.Multiline = True
        Me.txtAjeitaLog.Name = "txtAjeitaLog"
        Me.txtAjeitaLog.ScrollBars = System.Windows.Forms.ScrollBars.Vertical
        Me.txtAjeitaLog.Size = New System.Drawing.Size(606, 309)
        Me.txtAjeitaLog.TabIndex = 14
        '
        'btAjeitador
        '
        Me.btAjeitador.Location = New System.Drawing.Point(362, 16)
        Me.btAjeitador.Name = "btAjeitador"
        Me.btAjeitador.Size = New System.Drawing.Size(75, 19)
        Me.btAjeitador.TabIndex = 3
        Me.btAjeitador.Text = "Ajeitar"
        Me.btAjeitador.UseVisualStyleBackColor = True
        '
        'Label8
        '
        Me.Label8.AutoSize = True
        Me.Label8.Location = New System.Drawing.Point(3, 0)
        Me.Label8.Name = "Label8"
        Me.Label8.Size = New System.Drawing.Size(78, 13)
        Me.Label8.TabIndex = 2
        Me.Label8.Text = "Pasta Arquivos"
        '
        'txtPastasAjeitar
        '
        Me.txtPastasAjeitar.Location = New System.Drawing.Point(3, 16)
        Me.txtPastasAjeitar.Name = "txtPastasAjeitar"
        Me.txtPastasAjeitar.Size = New System.Drawing.Size(353, 20)
        Me.txtPastasAjeitar.TabIndex = 0
        '
        'TabPage3
        '
        Me.TabPage3.Controls.Add(Me.Label9)
        Me.TabPage3.Controls.Add(Me.txtPastaAfiliado1)
        Me.TabPage3.Controls.Add(Me.BtAfiliado1)
        Me.TabPage3.Controls.Add(Me.txtGalerias1)
        Me.TabPage3.Location = New System.Drawing.Point(4, 22)
        Me.TabPage3.Name = "TabPage3"
        Me.TabPage3.Size = New System.Drawing.Size(606, 437)
        Me.TabPage3.TabIndex = 4
        Me.TabPage3.Text = "Afiliado:Babebay"
        Me.TabPage3.UseVisualStyleBackColor = True
        '
        'Label9
        '
        Me.Label9.AutoSize = True
        Me.Label9.Location = New System.Drawing.Point(8, 302)
        Me.Label9.Name = "Label9"
        Me.Label9.Size = New System.Drawing.Size(73, 13)
        Me.Label9.TabIndex = 10
        Me.Label9.Text = "Pasta Destino"
        '
        'txtPastaAfiliado1
        '
        Me.txtPastaAfiliado1.Location = New System.Drawing.Point(8, 318)
        Me.txtPastaAfiliado1.Name = "txtPastaAfiliado1"
        Me.txtPastaAfiliado1.Size = New System.Drawing.Size(353, 20)
        Me.txtPastaAfiliado1.TabIndex = 9
        Me.txtPastaAfiliado1.Text = "K:\staging\pictuly\upload-galerias"
        '
        'BtAfiliado1
        '
        Me.BtAfiliado1.Location = New System.Drawing.Point(8, 359)
        Me.BtAfiliado1.Name = "BtAfiliado1"
        Me.BtAfiliado1.Size = New System.Drawing.Size(75, 24)
        Me.BtAfiliado1.TabIndex = 8
        Me.BtAfiliado1.Text = "Cataloga"
        Me.BtAfiliado1.UseVisualStyleBackColor = True
        '
        'txtGalerias1
        '
        Me.txtGalerias1.Dock = System.Windows.Forms.DockStyle.Top
        Me.txtGalerias1.Location = New System.Drawing.Point(0, 0)
        Me.txtGalerias1.Multiline = True
        Me.txtGalerias1.Name = "txtGalerias1"
        Me.txtGalerias1.Size = New System.Drawing.Size(606, 301)
        Me.txtGalerias1.TabIndex = 7
        Me.txtGalerias1.Text = resources.GetString("txtGalerias1.Text")
        '
        'TextBox2
        '
        Me.TextBox2.Dock = System.Windows.Forms.DockStyle.Top
        Me.TextBox2.Location = New System.Drawing.Point(0, 0)
        Me.TextBox2.Multiline = True
        Me.TextBox2.Name = "TextBox2"
        Me.TextBox2.Size = New System.Drawing.Size(843, 212)
        Me.TextBox2.TabIndex = 4
        Me.TextBox2.Text = "all" & Global.Microsoft.VisualBasic.ChrW(13) & Global.Microsoft.VisualBasic.ChrW(10) & "image"
        '
        'frmCatalogador
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(614, 463)
        Me.Controls.Add(Me.TabControl)
        Me.Name = "frmCatalogador"
        Me.Text = "Catalogador"
        Me.TabControl.ResumeLayout(False)
        Me.tabPastas.ResumeLayout(False)
        Me.tabPastas.PerformLayout()
        Me.TabPage1.ResumeLayout(False)
        Me.TabPage1.PerformLayout()
        Me.TabPage2.ResumeLayout(False)
        Me.TabPage2.PerformLayout()
        Me.TabAjeitador.ResumeLayout(False)
        Me.TabAjeitador.PerformLayout()
        Me.TabPage3.ResumeLayout(False)
        Me.TabPage3.PerformLayout()
        Me.ResumeLayout(False)

    End Sub
    Friend WithEvents TabControl As System.Windows.Forms.TabControl
    Friend WithEvents TabPage1 As System.Windows.Forms.TabPage
    Friend WithEvents TabPage2 As System.Windows.Forms.TabPage
    Friend WithEvents txtConfig As System.Windows.Forms.TextBox
    Friend WithEvents Label1 As System.Windows.Forms.Label
    Friend WithEvents txtFolder As System.Windows.Forms.TextBox
    Friend WithEvents btSelecionaFolder As System.Windows.Forms.Button
    Friend WithEvents FolderBrowser As System.Windows.Forms.FolderBrowserDialog
    Friend WithEvents btCataloga As System.Windows.Forms.Button
    Friend WithEvents Label2 As System.Windows.Forms.Label
    Friend WithEvents txtTags As System.Windows.Forms.TextBox
    Friend WithEvents txtSaida As System.Windows.Forms.TextBox
    Friend WithEvents checkNSFW As System.Windows.Forms.CheckBox
    Friend WithEvents Label3 As System.Windows.Forms.Label
    Friend WithEvents txtIdUsuario As System.Windows.Forms.TextBox
    Friend WithEvents Label4 As System.Windows.Forms.Label
    Friend WithEvents txtNiveis As System.Windows.Forms.TextBox
    Friend WithEvents txtLog As System.Windows.Forms.TextBox
    Friend WithEvents checkCancel As System.Windows.Forms.CheckBox
    Friend WithEvents Label5 As System.Windows.Forms.Label
    Friend WithEvents txtUrlOriginal As System.Windows.Forms.TextBox
    Friend WithEvents Label6 As System.Windows.Forms.Label
    Friend WithEvents txtTextoOriginal As System.Windows.Forms.TextBox
    Friend WithEvents Label7 As System.Windows.Forms.Label
    Friend WithEvents txtPartner As System.Windows.Forms.TextBox
    Friend WithEvents tabPastas As System.Windows.Forms.TabPage
    Friend WithEvents txtPastas As System.Windows.Forms.TextBox
    Friend WithEvents btCatalogaPasta As System.Windows.Forms.Button
    Friend WithEvents TextBox2 As System.Windows.Forms.TextBox
    Friend WithEvents TabAjeitador As System.Windows.Forms.TabPage
    Friend WithEvents txtAjeitaLog As System.Windows.Forms.TextBox
    Friend WithEvents btAjeitador As System.Windows.Forms.Button
    Friend WithEvents Label8 As System.Windows.Forms.Label
    Friend WithEvents txtPastasAjeitar As System.Windows.Forms.TextBox
    Friend WithEvents btRetageia As System.Windows.Forms.Button
    Friend WithEvents TabPage3 As System.Windows.Forms.TabPage
    Friend WithEvents Label9 As System.Windows.Forms.Label
    Friend WithEvents txtPastaAfiliado1 As System.Windows.Forms.TextBox
    Friend WithEvents BtAfiliado1 As System.Windows.Forms.Button
    Friend WithEvents txtGalerias1 As System.Windows.Forms.TextBox

End Class
