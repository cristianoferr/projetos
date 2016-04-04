Public Class FrmPrincipal
    Dim util As util
    Dim workDir As String
    Dim local As LocalClient = Nothing
    Public loader As BlueprintLoader
    Dim blueprintController As BluePrintController
    Dim prevBP As String = ""

    Dim editor1 As String = ""
    Dim editor2 As String = ""
    Dim editor3 As String = ""
    Dim editor4 As String = ""
    Public baseElement As String = "base"

    Public Sub New()

        ' This call is required by the designer.
        InitializeComponent()

        ' Add any initialization after the InitializeComponent() call.

    End Sub

    Private Sub FrmPrincipal_Shown(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Shown
        local = New LocalClient()
        workDir = local.getCurrDir
        util = New util(workDir, "config.ini")
        blueprintController = New BluePrintController(Me)
        PanelBlueprints.Controls.Add(blueprintController)
        'blueprintController.startWith("@blueprint teste:root " & Chr(13) & "@property tipo='teste de tipo'" & Chr(13) & "@property valor=10" & Chr(13) & "@orientation angle=350" & Chr(13) & "@position xpto=12233" & Chr(13) & "@property valInc+='aabc'" & Chr(13) & "@end")
        blueprintController.Dock = DockStyle.Fill
        txtTags.Text = util.getPar("last_busca_tag")
        editor1 = util.getPar("editorPath1")
        editor2 = util.getPar("editorPath2")
        editor3 = util.getPar("editorPath3")
        editor4 = util.getPar("editorPath4")
        carrega()
        TimerModify.Enabled = True


    End Sub

    Private Sub btCarrega_Click(sender As System.Object, e As System.EventArgs)
        carrega()
    End Sub

    Sub limpaTodo()
        listTodo.Items.Clear()
        tabTodo.Text = "TODO"
    End Sub

    Sub addTodo(todo As String)
        listTodo.Items.Add(todo)
        tabTodo.Text = "TODO [" & listTodo.Items.Count & "]"
    End Sub

    Sub carrega()
        Dim rootfile As String = util.getPar("rootfile")
        Dim par1 = "file="
        Dim parBase = "base="
        For Each s As String In My.Application.CommandLineArgs
            If s.ToLower.StartsWith(par1) Then
                rootfile = s.Remove(0, par1.Length)
            End If
            If s.ToLower.StartsWith(parBase) Then
                baseElement = s.Remove(0, parBase.Length)
            End If
        Next

        loader = New BlueprintLoader(local, util.getPar("rootdir"), rootfile, baseElement, Me)

        atualiza()

        lblTotal.Text = loader.count
        Text = rootfile & " - BlueprintManager"
    End Sub

    Public Sub atualiza()
        iniciaTree(baseElement)
        carregaDefines()
        Dim node As String = util.getPar("last_node")
        If node <> "" Then
            selecionaNode(node, True)
        End If
    End Sub

    Sub carregaDefines()
        Dim defines As ArrayList = loader.getDefines()
        Dim define As Define
        txtDefines.Text = ""
        For Each define In defines
            txtDefines.Text = txtDefines.Text & define.generate & Chr(13) & Chr(10)
        Next
    End Sub


    Sub iniciaTree(bpRoot As String)
        bpTree.Nodes.Clear()
        blueprintController.cmbExtends.Items.Clear()
        Dim bp As Blueprint = loader.getBPWithName(bpRoot)
        If (bp Is Nothing) Then Return
        Dim node As TreeNode = New TreeNode(bp.nome)
        addNodes(bp, node)
        bpTree.Nodes.Add(node)
    End Sub

    Sub addNodes(bp As Blueprint, nodeRoot As TreeNode)
        Dim bpFilho As Blueprint
        blueprintController.cmbExtends.Items.Add(bp.nome)

        For Each bpFilho In bp.getChildren()
            Dim node As TreeNode = New TreeNode(bpFilho.nome)
            If bpFilho.tipo = "factory" Then
                node.ForeColor = Color.Blue
            End If
            If bpFilho.tipo = "mod" Then
                node.ForeColor = Color.Red
            End If
            nodeRoot.Nodes.Add(node)
            addNodes(bpFilho, node)
        Next
    End Sub

    Private Sub bpTree_NodeMouseClick(sender As System.Object, e As System.Windows.Forms.TreeNodeMouseClickEventArgs) Handles bpTree.NodeMouseClick
        selecionaNode(e.Node.Text, False)

    End Sub
    Sub selecionaNode(nome As String, flagReloadIgual As Boolean)
        Dim bp As Blueprint = loader.getBPWithName(nome)
        If Not flagReloadIgual Then
            If prevBP.ToLower = nome.ToLower Then Return
            prevBP = nome
        End If
        util.setPar("last_node", nome)
        If (Not bp Is Nothing) Then
            blueprintController.loadFrom(bp)
        End If
        If bpTree.Nodes.Count = 0 Then Return
        chooseNode(bpTree.Nodes(0), nome)
    End Sub

    Sub chooseNode(node As TreeNode, nome As String)
        If node.Text.ToLower = nome.ToLower Then
            bpTree.SelectedNode = node
            '  bpTree.s()
        Else
            Dim n As TreeNode
            For Each n In node.Nodes
                chooseNode(n, nome)
            Next
        End If
    End Sub

    Private Sub bpTree_KeyDown(sender As System.Object, e As System.Windows.Forms.KeyEventArgs) Handles bpTree.KeyUp
        If bpTree.SelectedNode Is Nothing Then Return
        selecionaNode(bpTree.SelectedNode.Text, False)
    End Sub

    Public Sub salva()
        loader.atualizaDefines(txtDefines.Text)
        loader.salva(loader.rootdir, loader.rootfile)
    End Sub

    Private Sub NovaBlueprintToolStripMenuItem_Click(sender As System.Object, e As System.EventArgs) Handles NovaBlueprintToolStripMenuItem.Click
        adicionaBlueprint("blueprint", bpTree.SelectedNode.Text)
    End Sub
    Sub adicionaBlueprint(tipo As String, pai As String)
        Dim nome = InputBox("Nome da " + tipo, "Novo Item")
        Dim bp As Blueprint = New Blueprint(nome, pai, tipo)
        loader.addBlueprint(bp)
        loader.linkaBlueprints()
        atualiza()
        selecionaNode(nome, True)
    End Sub

    Private Sub NovaFactoryToolStripMenuItem_Click(sender As System.Object, e As System.EventArgs) Handles NovaFactoryToolStripMenuItem.Click
        adicionaBlueprint("factory", bpTree.SelectedNode.Text)
    End Sub

    Private Sub NovoModToolStripMenuItem_Click(sender As System.Object, e As System.EventArgs) Handles NovoModToolStripMenuItem.Click
        adicionaBlueprint("mod", bpTree.SelectedNode.Text)
    End Sub

    Private Sub RemoveItemToolStripMenuItem_Click(sender As System.Object, e As System.EventArgs) Handles RemoveItemToolStripMenuItem.Click
        Dim bp As Blueprint = loader.getBPWithName(bpTree.SelectedNode.Text)
        loader.removeBlueprint(bpTree.SelectedNode.Text)
        atualiza()
        If (Not bp Is Nothing) Then
            selecionaNode(bp.extends, True)
        End If
    End Sub

    Private Sub txtCode_Validated(sender As System.Object, e As System.EventArgs) Handles txtCode.Validated
        If txtCode.Text = txtCode.Tag Then
            Return
        End If
        txtCode.Tag = txtCode.Text
        Dim bp As Blueprint = loader.getBPWithName(blueprintController.blueprint.nome)

        'loader.removeBlueprint(blueprintController.blueprint.nome)
        'Dim bp As Blueprint = blueprintController.parseBlueprint(txtCode.Text)
        blueprintController.atualizaBlueprintFromCode(bp, txtCode.Text, Me)
        'loader.addBlueprint(bp)
        atualiza()
        selecionaNode(bp.nome, True)
    End Sub


    Private Sub btCarrega_Click_2(sender As System.Object, e As System.EventArgs) Handles btCarrega.Click
        carrega()
    End Sub

    Private Sub btSalva_Click_1(sender As System.Object, e As System.EventArgs) Handles btSalva.Click
        salva()
    End Sub

    Private Sub FrmPrincipal_FormClosed(sender As System.Object, e As System.Windows.Forms.FormClosedEventArgs) Handles MyBase.FormClosed
        salva()
    End Sub

    Private Sub btBuscaTags_Click(sender As System.Object, e As System.EventArgs) Handles btBuscaTags.Click
        buscaTags()

        
    End Sub

    Sub buscaTags()
        util.setPar("last_busca_tag", txtTags.Text)
        Dim arr As ArrayList = loader.filtraPorTag(txtTags.Text)
        Dim bp As Blueprint
        listTags.Items.Clear()
        For Each bp In arr
            listTags.Items.Add(bp.nome)
        Next
    End Sub
    Private Sub txtTags_KeyUp(sender As System.Object, e As System.Windows.Forms.KeyEventArgs) Handles txtTags.KeyUp
        buscaTags()
    End Sub

    Private Sub listTags_SelectedIndexChanged(sender As System.Object, e As System.EventArgs) Handles listTags.SelectedIndexChanged
        Dim atual As String = listTags.SelectedItem.ToString
        selecionaNode(atual, False)
    End Sub

    Private Sub txtComment_Validated(sender As System.Object, e As System.EventArgs) Handles txtComment.Validated
        If txtComment.Text = txtComment.Tag Then
            Return
        End If
        txtComment.Tag = txtComment.Text

        Dim bp As Blueprint = blueprintController.blueprint
        If bp Is Nothing Then Return
        bp.setParamAsText("comment", "description", "=", txtComment.Text)

    End Sub

    

    Private Sub TimerModify_Tick(sender As System.Object, e As System.EventArgs) Handles TimerModify.Tick
        If (loader.arquivoMudou()) Then
            TimerModify.Enabled = False
            carrega()
            salva()
            TimerModify.Enabled = True
        End If
    End Sub

    Private Sub btEdita_Click(sender As System.Object, e As System.EventArgs) Handles btEdita.Click
        If local.FileExists(editor1) Then
            Shell(editor1 & " " & loader.rootdir & "\" & loader.rootfile, AppWinStyle.NormalFocus, False)
        ElseIf local.FileExists(editor2) Then
            Shell(editor2 & " " & loader.rootdir & "\" & loader.rootfile, AppWinStyle.NormalFocus, False)
        ElseIf local.FileExists(editor3) Then
            Shell(editor3 & " " & loader.rootdir & "\" & loader.rootfile, AppWinStyle.NormalFocus, False)
        ElseIf local.FileExists(editor4) Then
            Shell(editor4 & " " & loader.rootdir & "\" & loader.rootfile, AppWinStyle.NormalFocus, False)
        End If
    End Sub
End Class
