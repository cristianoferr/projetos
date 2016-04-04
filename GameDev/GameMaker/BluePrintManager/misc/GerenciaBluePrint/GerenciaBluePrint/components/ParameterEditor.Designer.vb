<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class parameterEditor
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
        Me.txtIdentifier = New System.Windows.Forms.TextBox()
        Me.comboModifier = New System.Windows.Forms.ComboBox()
        Me.txtParam = New System.Windows.Forms.TextBox()
        Me.SuspendLayout()
        '
        'txtIdentifier
        '
        Me.txtIdentifier.Dock = System.Windows.Forms.DockStyle.Left
        Me.txtIdentifier.Location = New System.Drawing.Point(0, 0)
        Me.txtIdentifier.Name = "txtIdentifier"
        Me.txtIdentifier.Size = New System.Drawing.Size(161, 20)
        Me.txtIdentifier.TabIndex = 0
        Me.txtIdentifier.Text = "asd"
        '
        'comboModifier
        '
        Me.comboModifier.Dock = System.Windows.Forms.DockStyle.Left
        Me.comboModifier.FormattingEnabled = True
        Me.comboModifier.Items.AddRange(New Object() {"=", "+=", "*=", "-=", "/="})
        Me.comboModifier.Location = New System.Drawing.Point(161, 0)
        Me.comboModifier.Name = "comboModifier"
        Me.comboModifier.Size = New System.Drawing.Size(37, 21)
        Me.comboModifier.TabIndex = 1
        Me.comboModifier.Text = "="
        '
        'txtParam
        '
        Me.txtParam.Dock = System.Windows.Forms.DockStyle.Fill
        Me.txtParam.Location = New System.Drawing.Point(198, 0)
        Me.txtParam.Name = "txtParam"
        Me.txtParam.Size = New System.Drawing.Size(45, 20)
        Me.txtParam.TabIndex = 2
        '
        'parameterEditor
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.BackColor = System.Drawing.SystemColors.ControlDark
        Me.Controls.Add(Me.txtParam)
        Me.Controls.Add(Me.comboModifier)
        Me.Controls.Add(Me.txtIdentifier)
        Me.Name = "parameterEditor"
        Me.Size = New System.Drawing.Size(243, 20)
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub
    Friend WithEvents txtIdentifier As System.Windows.Forms.TextBox
    Friend WithEvents comboModifier As System.Windows.Forms.ComboBox
    Friend WithEvents txtParam As System.Windows.Forms.TextBox

End Class
