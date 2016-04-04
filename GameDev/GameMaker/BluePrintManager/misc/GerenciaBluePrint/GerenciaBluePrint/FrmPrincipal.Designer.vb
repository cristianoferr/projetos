<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class FrmPrincipal
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
        Me.components = New System.ComponentModel.Container()
        Me.Panel1 = New System.Windows.Forms.Panel()
        Me.TabControl1 = New System.Windows.Forms.TabControl()
        Me.TabPage1 = New System.Windows.Forms.TabPage()
        Me.bpTree = New System.Windows.Forms.TreeView()
        Me.menuTree = New System.Windows.Forms.ContextMenuStrip(Me.components)
        Me.NovaBlueprintToolStripMenuItem = New System.Windows.Forms.ToolStripMenuItem()
        Me.NovaFactoryToolStripMenuItem = New System.Windows.Forms.ToolStripMenuItem()
        Me.NovoModToolStripMenuItem = New System.Windows.Forms.ToolStripMenuItem()
        Me.ToolStripSeparator1 = New System.Windows.Forms.ToolStripSeparator()
        Me.RemoveItemToolStripMenuItem = New System.Windows.Forms.ToolStripMenuItem()
        Me.TabPage2 = New System.Windows.Forms.TabPage()
        Me.txtDefines = New System.Windows.Forms.TextBox()
        Me.TabPage3 = New System.Windows.Forms.TabPage()
        Me.listTags = New System.Windows.Forms.ListBox()
        Me.btBuscaTags = New System.Windows.Forms.Button()
        Me.txtTags = New System.Windows.Forms.TextBox()
        Me.Label2 = New System.Windows.Forms.Label()
        Me.tabTodo = New System.Windows.Forms.TabPage()
        Me.listTodo = New System.Windows.Forms.ListBox()
        Me.btSalva = New System.Windows.Forms.Button()
        Me.btCarrega = New System.Windows.Forms.Button()
        Me.PanelLeftBottom = New System.Windows.Forms.Panel()
        Me.lblTotal = New System.Windows.Forms.Label()
        Me.Label1 = New System.Windows.Forms.Label()
        Me.Panel2 = New System.Windows.Forms.Panel()
        Me.PanelBlueprints = New System.Windows.Forms.Panel()
        Me.txtComment = New System.Windows.Forms.TextBox()
        Me.PanelTop = New System.Windows.Forms.Panel()
        Me.btEdita = New System.Windows.Forms.Button()
        Me.txtCode = New System.Windows.Forms.RichTextBox()
        Me.TimerModify = New System.Windows.Forms.Timer(Me.components)
        Me.Panel1.SuspendLayout()
        Me.TabControl1.SuspendLayout()
        Me.TabPage1.SuspendLayout()
        Me.menuTree.SuspendLayout()
        Me.TabPage2.SuspendLayout()
        Me.TabPage3.SuspendLayout()
        Me.tabTodo.SuspendLayout()
        Me.PanelLeftBottom.SuspendLayout()
        Me.Panel2.SuspendLayout()
        Me.PanelTop.SuspendLayout()
        Me.SuspendLayout()
        '
        'Panel1
        '
        Me.Panel1.Controls.Add(Me.TabControl1)
        Me.Panel1.Controls.Add(Me.btSalva)
        Me.Panel1.Controls.Add(Me.btCarrega)
        Me.Panel1.Controls.Add(Me.PanelLeftBottom)
        Me.Panel1.Dock = System.Windows.Forms.DockStyle.Left
        Me.Panel1.Location = New System.Drawing.Point(0, 0)
        Me.Panel1.Name = "Panel1"
        Me.Panel1.Size = New System.Drawing.Size(229, 521)
        Me.Panel1.TabIndex = 1
        '
        'TabControl1
        '
        Me.TabControl1.Controls.Add(Me.TabPage1)
        Me.TabControl1.Controls.Add(Me.TabPage2)
        Me.TabControl1.Controls.Add(Me.TabPage3)
        Me.TabControl1.Controls.Add(Me.tabTodo)
        Me.TabControl1.Dock = System.Windows.Forms.DockStyle.Fill
        Me.TabControl1.Location = New System.Drawing.Point(0, 46)
        Me.TabControl1.Name = "TabControl1"
        Me.TabControl1.SelectedIndex = 0
        Me.TabControl1.Size = New System.Drawing.Size(229, 434)
        Me.TabControl1.TabIndex = 12
        '
        'TabPage1
        '
        Me.TabPage1.Controls.Add(Me.bpTree)
        Me.TabPage1.Location = New System.Drawing.Point(4, 22)
        Me.TabPage1.Name = "TabPage1"
        Me.TabPage1.Padding = New System.Windows.Forms.Padding(3)
        Me.TabPage1.Size = New System.Drawing.Size(221, 408)
        Me.TabPage1.TabIndex = 0
        Me.TabPage1.Text = "BPs"
        Me.TabPage1.UseVisualStyleBackColor = True
        '
        'bpTree
        '
        Me.bpTree.BackColor = System.Drawing.SystemColors.Window
        Me.bpTree.ContextMenuStrip = Me.menuTree
        Me.bpTree.Dock = System.Windows.Forms.DockStyle.Fill
        Me.bpTree.Location = New System.Drawing.Point(3, 3)
        Me.bpTree.Name = "bpTree"
        Me.bpTree.Size = New System.Drawing.Size(215, 402)
        Me.bpTree.TabIndex = 10
        '
        'menuTree
        '
        Me.menuTree.Items.AddRange(New System.Windows.Forms.ToolStripItem() {Me.NovaBlueprintToolStripMenuItem, Me.NovaFactoryToolStripMenuItem, Me.NovoModToolStripMenuItem, Me.ToolStripSeparator1, Me.RemoveItemToolStripMenuItem})
        Me.menuTree.Name = "menuTree"
        Me.menuTree.Size = New System.Drawing.Size(154, 98)
        '
        'NovaBlueprintToolStripMenuItem
        '
        Me.NovaBlueprintToolStripMenuItem.Name = "NovaBlueprintToolStripMenuItem"
        Me.NovaBlueprintToolStripMenuItem.Size = New System.Drawing.Size(153, 22)
        Me.NovaBlueprintToolStripMenuItem.Text = "Nova Blueprint"
        '
        'NovaFactoryToolStripMenuItem
        '
        Me.NovaFactoryToolStripMenuItem.Name = "NovaFactoryToolStripMenuItem"
        Me.NovaFactoryToolStripMenuItem.Size = New System.Drawing.Size(153, 22)
        Me.NovaFactoryToolStripMenuItem.Text = "Nova Factory"
        '
        'NovoModToolStripMenuItem
        '
        Me.NovoModToolStripMenuItem.Name = "NovoModToolStripMenuItem"
        Me.NovoModToolStripMenuItem.Size = New System.Drawing.Size(153, 22)
        Me.NovoModToolStripMenuItem.Text = "Novo Mod"
        '
        'ToolStripSeparator1
        '
        Me.ToolStripSeparator1.Name = "ToolStripSeparator1"
        Me.ToolStripSeparator1.Size = New System.Drawing.Size(150, 6)
        '
        'RemoveItemToolStripMenuItem
        '
        Me.RemoveItemToolStripMenuItem.Name = "RemoveItemToolStripMenuItem"
        Me.RemoveItemToolStripMenuItem.Size = New System.Drawing.Size(153, 22)
        Me.RemoveItemToolStripMenuItem.Text = "Remove Item"
        '
        'TabPage2
        '
        Me.TabPage2.Controls.Add(Me.txtDefines)
        Me.TabPage2.Location = New System.Drawing.Point(4, 22)
        Me.TabPage2.Name = "TabPage2"
        Me.TabPage2.Padding = New System.Windows.Forms.Padding(3)
        Me.TabPage2.Size = New System.Drawing.Size(221, 408)
        Me.TabPage2.TabIndex = 1
        Me.TabPage2.Text = "Defines"
        Me.TabPage2.UseVisualStyleBackColor = True
        '
        'txtDefines
        '
        Me.txtDefines.Dock = System.Windows.Forms.DockStyle.Fill
        Me.txtDefines.Location = New System.Drawing.Point(3, 3)
        Me.txtDefines.Multiline = True
        Me.txtDefines.Name = "txtDefines"
        Me.txtDefines.Size = New System.Drawing.Size(215, 402)
        Me.txtDefines.TabIndex = 0
        '
        'TabPage3
        '
        Me.TabPage3.BackColor = System.Drawing.Color.Transparent
        Me.TabPage3.Controls.Add(Me.listTags)
        Me.TabPage3.Controls.Add(Me.btBuscaTags)
        Me.TabPage3.Controls.Add(Me.txtTags)
        Me.TabPage3.Controls.Add(Me.Label2)
        Me.TabPage3.Location = New System.Drawing.Point(4, 22)
        Me.TabPage3.Name = "TabPage3"
        Me.TabPage3.Size = New System.Drawing.Size(221, 408)
        Me.TabPage3.TabIndex = 2
        Me.TabPage3.Text = "Tags"
        '
        'listTags
        '
        Me.listTags.Dock = System.Windows.Forms.DockStyle.Fill
        Me.listTags.FormattingEnabled = True
        Me.listTags.Location = New System.Drawing.Point(0, 56)
        Me.listTags.Name = "listTags"
        Me.listTags.Size = New System.Drawing.Size(221, 352)
        Me.listTags.TabIndex = 3
        '
        'btBuscaTags
        '
        Me.btBuscaTags.Dock = System.Windows.Forms.DockStyle.Top
        Me.btBuscaTags.Location = New System.Drawing.Point(0, 33)
        Me.btBuscaTags.Name = "btBuscaTags"
        Me.btBuscaTags.Size = New System.Drawing.Size(221, 23)
        Me.btBuscaTags.TabIndex = 2
        Me.btBuscaTags.Text = "Busca"
        Me.btBuscaTags.UseVisualStyleBackColor = True
        '
        'txtTags
        '
        Me.txtTags.Dock = System.Windows.Forms.DockStyle.Top
        Me.txtTags.Location = New System.Drawing.Point(0, 13)
        Me.txtTags.Name = "txtTags"
        Me.txtTags.Size = New System.Drawing.Size(221, 20)
        Me.txtTags.TabIndex = 1
        '
        'Label2
        '
        Me.Label2.AutoSize = True
        Me.Label2.Dock = System.Windows.Forms.DockStyle.Top
        Me.Label2.Location = New System.Drawing.Point(0, 0)
        Me.Label2.Name = "Label2"
        Me.Label2.Size = New System.Drawing.Size(31, 13)
        Me.Label2.TabIndex = 0
        Me.Label2.Text = "Tags"
        '
        'tabTodo
        '
        Me.tabTodo.Controls.Add(Me.listTodo)
        Me.tabTodo.Location = New System.Drawing.Point(4, 22)
        Me.tabTodo.Name = "tabTodo"
        Me.tabTodo.Size = New System.Drawing.Size(221, 408)
        Me.tabTodo.TabIndex = 3
        Me.tabTodo.Text = "TODO"
        Me.tabTodo.UseVisualStyleBackColor = True
        '
        'listTodo
        '
        Me.listTodo.Dock = System.Windows.Forms.DockStyle.Fill
        Me.listTodo.FormattingEnabled = True
        Me.listTodo.Location = New System.Drawing.Point(0, 0)
        Me.listTodo.Name = "listTodo"
        Me.listTodo.Size = New System.Drawing.Size(221, 408)
        Me.listTodo.TabIndex = 4
        '
        'btSalva
        '
        Me.btSalva.Dock = System.Windows.Forms.DockStyle.Top
        Me.btSalva.Location = New System.Drawing.Point(0, 23)
        Me.btSalva.Name = "btSalva"
        Me.btSalva.Size = New System.Drawing.Size(229, 23)
        Me.btSalva.TabIndex = 11
        Me.btSalva.Text = "Salva"
        Me.btSalva.UseVisualStyleBackColor = True
        '
        'btCarrega
        '
        Me.btCarrega.Dock = System.Windows.Forms.DockStyle.Top
        Me.btCarrega.Location = New System.Drawing.Point(0, 0)
        Me.btCarrega.Name = "btCarrega"
        Me.btCarrega.Size = New System.Drawing.Size(229, 23)
        Me.btCarrega.TabIndex = 10
        Me.btCarrega.Text = "Carrega"
        Me.btCarrega.UseVisualStyleBackColor = True
        '
        'PanelLeftBottom
        '
        Me.PanelLeftBottom.Controls.Add(Me.lblTotal)
        Me.PanelLeftBottom.Controls.Add(Me.Label1)
        Me.PanelLeftBottom.Dock = System.Windows.Forms.DockStyle.Bottom
        Me.PanelLeftBottom.Location = New System.Drawing.Point(0, 480)
        Me.PanelLeftBottom.Name = "PanelLeftBottom"
        Me.PanelLeftBottom.Size = New System.Drawing.Size(229, 41)
        Me.PanelLeftBottom.TabIndex = 6
        '
        'lblTotal
        '
        Me.lblTotal.AutoSize = True
        Me.lblTotal.Dock = System.Windows.Forms.DockStyle.Right
        Me.lblTotal.Font = New System.Drawing.Font("Microsoft Sans Serif", 12.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.lblTotal.Location = New System.Drawing.Point(185, 0)
        Me.lblTotal.Name = "lblTotal"
        Me.lblTotal.Size = New System.Drawing.Size(44, 20)
        Me.lblTotal.TabIndex = 1
        Me.lblTotal.Text = "Total"
        '
        'Label1
        '
        Me.Label1.AutoSize = True
        Me.Label1.Dock = System.Windows.Forms.DockStyle.Left
        Me.Label1.Font = New System.Drawing.Font("Microsoft Sans Serif", 12.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label1.Location = New System.Drawing.Point(0, 0)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(80, 20)
        Me.Label1.TabIndex = 0
        Me.Label1.Text = "Blueprints"
        '
        'Panel2
        '
        Me.Panel2.Controls.Add(Me.PanelBlueprints)
        Me.Panel2.Controls.Add(Me.txtComment)
        Me.Panel2.Controls.Add(Me.PanelTop)
        Me.Panel2.Controls.Add(Me.txtCode)
        Me.Panel2.Dock = System.Windows.Forms.DockStyle.Fill
        Me.Panel2.Location = New System.Drawing.Point(229, 0)
        Me.Panel2.Name = "Panel2"
        Me.Panel2.Size = New System.Drawing.Size(650, 521)
        Me.Panel2.TabIndex = 3
        '
        'PanelBlueprints
        '
        Me.PanelBlueprints.Dock = System.Windows.Forms.DockStyle.Fill
        Me.PanelBlueprints.Location = New System.Drawing.Point(0, 23)
        Me.PanelBlueprints.Name = "PanelBlueprints"
        Me.PanelBlueprints.Size = New System.Drawing.Size(505, 371)
        Me.PanelBlueprints.TabIndex = 1
        '
        'txtComment
        '
        Me.txtComment.Dock = System.Windows.Forms.DockStyle.Right
        Me.txtComment.Location = New System.Drawing.Point(505, 23)
        Me.txtComment.Multiline = True
        Me.txtComment.Name = "txtComment"
        Me.txtComment.ScrollBars = System.Windows.Forms.ScrollBars.Both
        Me.txtComment.Size = New System.Drawing.Size(145, 371)
        Me.txtComment.TabIndex = 4
        '
        'PanelTop
        '
        Me.PanelTop.Controls.Add(Me.btEdita)
        Me.PanelTop.Dock = System.Windows.Forms.DockStyle.Top
        Me.PanelTop.Location = New System.Drawing.Point(0, 0)
        Me.PanelTop.Name = "PanelTop"
        Me.PanelTop.Size = New System.Drawing.Size(650, 23)
        Me.PanelTop.TabIndex = 3
        '
        'btEdita
        '
        Me.btEdita.Dock = System.Windows.Forms.DockStyle.Left
        Me.btEdita.Location = New System.Drawing.Point(0, 0)
        Me.btEdita.Name = "btEdita"
        Me.btEdita.Size = New System.Drawing.Size(94, 23)
        Me.btEdita.TabIndex = 11
        Me.btEdita.Text = "Editor de Texto"
        Me.btEdita.UseVisualStyleBackColor = True
        '
        'txtCode
        '
        Me.txtCode.Dock = System.Windows.Forms.DockStyle.Bottom
        Me.txtCode.Location = New System.Drawing.Point(0, 394)
        Me.txtCode.Name = "txtCode"
        Me.txtCode.Size = New System.Drawing.Size(650, 127)
        Me.txtCode.TabIndex = 2
        Me.txtCode.Text = ""
        Me.txtCode.WordWrap = False
        '
        'TimerModify
        '
        Me.TimerModify.Interval = 10000
        '
        'FrmPrincipal
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(879, 521)
        Me.Controls.Add(Me.Panel2)
        Me.Controls.Add(Me.Panel1)
        Me.Name = "FrmPrincipal"
        Me.Text = "Blueprints"
        Me.Panel1.ResumeLayout(False)
        Me.TabControl1.ResumeLayout(False)
        Me.TabPage1.ResumeLayout(False)
        Me.menuTree.ResumeLayout(False)
        Me.TabPage2.ResumeLayout(False)
        Me.TabPage2.PerformLayout()
        Me.TabPage3.ResumeLayout(False)
        Me.TabPage3.PerformLayout()
        Me.tabTodo.ResumeLayout(False)
        Me.PanelLeftBottom.ResumeLayout(False)
        Me.PanelLeftBottom.PerformLayout()
        Me.Panel2.ResumeLayout(False)
        Me.Panel2.PerformLayout()
        Me.PanelTop.ResumeLayout(False)
        Me.ResumeLayout(False)

    End Sub
    Friend WithEvents Panel1 As System.Windows.Forms.Panel
    Friend WithEvents menuTree As System.Windows.Forms.ContextMenuStrip
    Friend WithEvents NovaBlueprintToolStripMenuItem As System.Windows.Forms.ToolStripMenuItem
    Friend WithEvents NovaFactoryToolStripMenuItem As System.Windows.Forms.ToolStripMenuItem
    Friend WithEvents NovoModToolStripMenuItem As System.Windows.Forms.ToolStripMenuItem
    Friend WithEvents RemoveItemToolStripMenuItem As System.Windows.Forms.ToolStripMenuItem
    Friend WithEvents ToolStripSeparator1 As System.Windows.Forms.ToolStripSeparator
    Friend WithEvents Panel2 As System.Windows.Forms.Panel
    Friend WithEvents PanelBlueprints As System.Windows.Forms.Panel
    Friend WithEvents txtCode As System.Windows.Forms.RichTextBox
    Friend WithEvents btSalva As System.Windows.Forms.Button
    Friend WithEvents btCarrega As System.Windows.Forms.Button
    Friend WithEvents PanelLeftBottom As System.Windows.Forms.Panel
    Friend WithEvents lblTotal As System.Windows.Forms.Label
    Friend WithEvents Label1 As System.Windows.Forms.Label
    Friend WithEvents PanelTop As System.Windows.Forms.Panel
    Friend WithEvents TabControl1 As System.Windows.Forms.TabControl
    Friend WithEvents TabPage1 As System.Windows.Forms.TabPage
    Friend WithEvents bpTree As System.Windows.Forms.TreeView
    Friend WithEvents TabPage2 As System.Windows.Forms.TabPage
    Friend WithEvents txtDefines As System.Windows.Forms.TextBox
    Friend WithEvents TabPage3 As System.Windows.Forms.TabPage
    Friend WithEvents btBuscaTags As System.Windows.Forms.Button
    Friend WithEvents txtTags As System.Windows.Forms.TextBox
    Friend WithEvents Label2 As System.Windows.Forms.Label
    Friend WithEvents listTags As System.Windows.Forms.ListBox
    Friend WithEvents txtComment As System.Windows.Forms.TextBox
    Friend WithEvents btEdita As System.Windows.Forms.Button
    Friend WithEvents TimerModify As System.Windows.Forms.Timer
    Friend WithEvents tabTodo As System.Windows.Forms.TabPage
    Friend WithEvents listTodo As System.Windows.Forms.ListBox

End Class
