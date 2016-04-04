<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class BluePrintController
    Inherits System.Windows.Forms.UserControl

    'UserControl overrides dispose to clean up the component list.
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
        Me.Panel1 = New System.Windows.Forms.Panel()
        Me.cmbExtends = New System.Windows.Forms.ComboBox()
        Me.linkNovaLista = New System.Windows.Forms.LinkLabel()
        Me.Label4 = New System.Windows.Forms.Label()
        Me.Label2 = New System.Windows.Forms.Label()
        Me.Label3 = New System.Windows.Forms.Label()
        Me.txtName = New System.Windows.Forms.TextBox()
        Me.cmbType = New System.Windows.Forms.ComboBox()
        Me.Label1 = New System.Windows.Forms.Label()
        Me.panelConteudo = New System.Windows.Forms.Panel()
        Me.Panel1.SuspendLayout()
        Me.SuspendLayout()
        '
        'Panel1
        '
        Me.Panel1.BackColor = System.Drawing.SystemColors.GradientActiveCaption
        Me.Panel1.Controls.Add(Me.cmbExtends)
        Me.Panel1.Controls.Add(Me.linkNovaLista)
        Me.Panel1.Controls.Add(Me.Label4)
        Me.Panel1.Controls.Add(Me.Label2)
        Me.Panel1.Controls.Add(Me.Label3)
        Me.Panel1.Controls.Add(Me.txtName)
        Me.Panel1.Controls.Add(Me.cmbType)
        Me.Panel1.Controls.Add(Me.Label1)
        Me.Panel1.Dock = System.Windows.Forms.DockStyle.Top
        Me.Panel1.Location = New System.Drawing.Point(0, 0)
        Me.Panel1.Name = "Panel1"
        Me.Panel1.Size = New System.Drawing.Size(461, 31)
        Me.Panel1.TabIndex = 6
        '
        'cmbExtends
        '
        Me.cmbExtends.Dock = System.Windows.Forms.DockStyle.Left
        Me.cmbExtends.Font = New System.Drawing.Font("Microsoft Sans Serif", 12.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.cmbExtends.FormattingEnabled = True
        Me.cmbExtends.Location = New System.Drawing.Point(311, 0)
        Me.cmbExtends.Name = "cmbExtends"
        Me.cmbExtends.Size = New System.Drawing.Size(109, 28)
        Me.cmbExtends.TabIndex = 20
        '
        'linkNovaLista
        '
        Me.linkNovaLista.AutoSize = True
        Me.linkNovaLista.Dock = System.Windows.Forms.DockStyle.Right
        Me.linkNovaLista.Font = New System.Drawing.Font("Microsoft Sans Serif", 12.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.linkNovaLista.LinkColor = System.Drawing.Color.Red
        Me.linkNovaLista.Location = New System.Drawing.Point(378, 0)
        Me.linkNovaLista.Name = "linkNovaLista"
        Me.linkNovaLista.Size = New System.Drawing.Size(83, 20)
        Me.linkNovaLista.TabIndex = 16
        Me.linkNovaLista.TabStop = True
        Me.linkNovaLista.Text = "Nova Lista"
        '
        'Label4
        '
        Me.Label4.AutoSize = True
        Me.Label4.Dock = System.Windows.Forms.DockStyle.Left
        Me.Label4.Font = New System.Drawing.Font("Microsoft Sans Serif", 12.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label4.ForeColor = System.Drawing.SystemColors.ButtonHighlight
        Me.Label4.Location = New System.Drawing.Point(298, 0)
        Me.Label4.Name = "Label4"
        Me.Label4.Size = New System.Drawing.Size(13, 20)
        Me.Label4.TabIndex = 15
        Me.Label4.Text = ":"
        '
        'Label2
        '
        Me.Label2.AutoSize = True
        Me.Label2.Dock = System.Windows.Forms.DockStyle.Left
        Me.Label2.Font = New System.Drawing.Font("Microsoft Sans Serif", 9.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label2.Location = New System.Drawing.Point(298, 0)
        Me.Label2.Name = "Label2"
        Me.Label2.Size = New System.Drawing.Size(0, 15)
        Me.Label2.TabIndex = 13
        '
        'Label3
        '
        Me.Label3.AutoSize = True
        Me.Label3.Dock = System.Windows.Forms.DockStyle.Left
        Me.Label3.Font = New System.Drawing.Font("Microsoft Sans Serif", 9.0!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label3.Location = New System.Drawing.Point(298, 0)
        Me.Label3.Name = "Label3"
        Me.Label3.Size = New System.Drawing.Size(0, 15)
        Me.Label3.TabIndex = 14
        '
        'txtName
        '
        Me.txtName.Dock = System.Windows.Forms.DockStyle.Left
        Me.txtName.Font = New System.Drawing.Font("Microsoft Sans Serif", 14.25!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.txtName.Location = New System.Drawing.Point(134, 0)
        Me.txtName.Name = "txtName"
        Me.txtName.Size = New System.Drawing.Size(164, 29)
        Me.txtName.TabIndex = 19
        '
        'cmbType
        '
        Me.cmbType.Dock = System.Windows.Forms.DockStyle.Left
        Me.cmbType.Font = New System.Drawing.Font("Microsoft Sans Serif", 12.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.cmbType.FormattingEnabled = True
        Me.cmbType.Items.AddRange(New Object() {"blueprint", "mod", "factory"})
        Me.cmbType.Location = New System.Drawing.Point(25, 0)
        Me.cmbType.Name = "cmbType"
        Me.cmbType.Size = New System.Drawing.Size(109, 28)
        Me.cmbType.TabIndex = 18
        Me.cmbType.Text = "blueprint"
        '
        'Label1
        '
        Me.Label1.AutoSize = True
        Me.Label1.Dock = System.Windows.Forms.DockStyle.Left
        Me.Label1.Font = New System.Drawing.Font("Microsoft Sans Serif", 12.0!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label1.ForeColor = System.Drawing.SystemColors.ButtonHighlight
        Me.Label1.Location = New System.Drawing.Point(0, 0)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(25, 20)
        Me.Label1.TabIndex = 9
        Me.Label1.Text = "@"
        '
        'panelConteudo
        '
        Me.panelConteudo.AutoScroll = True
        Me.panelConteudo.BackColor = System.Drawing.SystemColors.ButtonFace
        Me.panelConteudo.Dock = System.Windows.Forms.DockStyle.Fill
        Me.panelConteudo.Location = New System.Drawing.Point(0, 31)
        Me.panelConteudo.Name = "panelConteudo"
        Me.panelConteudo.Size = New System.Drawing.Size(461, 166)
        Me.panelConteudo.TabIndex = 7
        '
        'BluePrintController
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.Controls.Add(Me.panelConteudo)
        Me.Controls.Add(Me.Panel1)
        Me.Name = "BluePrintController"
        Me.Size = New System.Drawing.Size(461, 197)
        Me.Panel1.ResumeLayout(False)
        Me.Panel1.PerformLayout()
        Me.ResumeLayout(False)

    End Sub
    Friend WithEvents Panel1 As System.Windows.Forms.Panel
    Friend WithEvents Label4 As System.Windows.Forms.Label
    Friend WithEvents Label2 As System.Windows.Forms.Label
    Friend WithEvents Label3 As System.Windows.Forms.Label
    Friend WithEvents Label1 As System.Windows.Forms.Label
    Friend WithEvents panelConteudo As System.Windows.Forms.Panel
    Friend WithEvents linkNovaLista As System.Windows.Forms.LinkLabel
    Friend WithEvents cmbType As System.Windows.Forms.ComboBox
    Friend WithEvents txtName As System.Windows.Forms.TextBox
    Friend WithEvents cmbExtends As System.Windows.Forms.ComboBox

End Class
