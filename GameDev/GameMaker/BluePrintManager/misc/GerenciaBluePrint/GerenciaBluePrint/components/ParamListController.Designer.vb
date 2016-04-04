<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class ParamListController
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
        Me.LinkLabel1 = New System.Windows.Forms.LinkLabel()
        Me.lblListName = New System.Windows.Forms.LinkLabel()
        Me.lblStatus = New System.Windows.Forms.Label()
        Me.containerFilho = New System.Windows.Forms.Panel()
        Me.Panel1.SuspendLayout()
        Me.SuspendLayout()
        '
        'Panel1
        '
        Me.Panel1.Controls.Add(Me.LinkLabel1)
        Me.Panel1.Controls.Add(Me.lblListName)
        Me.Panel1.Controls.Add(Me.lblStatus)
        Me.Panel1.Dock = System.Windows.Forms.DockStyle.Top
        Me.Panel1.Location = New System.Drawing.Point(0, 0)
        Me.Panel1.Name = "Panel1"
        Me.Panel1.Size = New System.Drawing.Size(150, 17)
        Me.Panel1.TabIndex = 2
        '
        'LinkLabel1
        '
        Me.LinkLabel1.AutoSize = True
        Me.LinkLabel1.Dock = System.Windows.Forms.DockStyle.Left
        Me.LinkLabel1.Location = New System.Drawing.Point(35, 0)
        Me.LinkLabel1.Name = "LinkLabel1"
        Me.LinkLabel1.Size = New System.Drawing.Size(58, 13)
        Me.LinkLabel1.TabIndex = 4
        Me.LinkLabel1.TabStop = True
        Me.LinkLabel1.Text = "Nova Prop"
        '
        'lblListName
        '
        Me.lblListName.AutoSize = True
        Me.lblListName.Dock = System.Windows.Forms.DockStyle.Left
        Me.lblListName.Location = New System.Drawing.Point(10, 0)
        Me.lblListName.Name = "lblListName"
        Me.lblListName.Size = New System.Drawing.Size(25, 13)
        Me.lblListName.TabIndex = 3
        Me.lblListName.TabStop = True
        Me.lblListName.Text = "lista"
        '
        'lblStatus
        '
        Me.lblStatus.AutoSize = True
        Me.lblStatus.Dock = System.Windows.Forms.DockStyle.Left
        Me.lblStatus.Location = New System.Drawing.Point(0, 0)
        Me.lblStatus.Name = "lblStatus"
        Me.lblStatus.Size = New System.Drawing.Size(10, 13)
        Me.lblStatus.TabIndex = 2
        Me.lblStatus.Text = "-"
        '
        'containerFilho
        '
        Me.containerFilho.Dock = System.Windows.Forms.DockStyle.Fill
        Me.containerFilho.Location = New System.Drawing.Point(0, 17)
        Me.containerFilho.Name = "containerFilho"
        Me.containerFilho.Size = New System.Drawing.Size(150, 127)
        Me.containerFilho.TabIndex = 3
        '
        'ParamListController
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.Controls.Add(Me.containerFilho)
        Me.Controls.Add(Me.Panel1)
        Me.Name = "ParamListController"
        Me.Size = New System.Drawing.Size(150, 144)
        Me.Panel1.ResumeLayout(False)
        Me.Panel1.PerformLayout()
        Me.ResumeLayout(False)

    End Sub
    Friend WithEvents Panel1 As System.Windows.Forms.Panel
    Friend WithEvents lblListName As System.Windows.Forms.LinkLabel
    Friend WithEvents lblStatus As System.Windows.Forms.Label
    Friend WithEvents containerFilho As System.Windows.Forms.Panel
    Friend WithEvents LinkLabel1 As System.Windows.Forms.LinkLabel

End Class
