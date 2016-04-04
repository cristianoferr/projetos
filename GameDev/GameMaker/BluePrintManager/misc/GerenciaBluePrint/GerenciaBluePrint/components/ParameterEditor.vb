Public Class parameterEditor
    Dim vParametro As Parametro
    Dim vBlueprintController As BluePrintController
    Dim blueprint As Blueprint
    Dim vParamList As ParamList
    Dim updating As Boolean = False

    Public Sub New(ByVal param As Parametro, ByVal blueprintController As BluePrintController, bp As Blueprint, ByVal pl As ParamList)
        InitializeComponent()
        Me.vParametro = param
        vBlueprintController = blueprintController
        vParamList = pl
        blueprint = bp
        updateFromModel()
    End Sub

    Sub updateFromModel()

        updating = True
        If (txtIdentifier.Text <> vParametro.identifier) Then
            txtIdentifier.Text = vParametro.identifier
            txtIdentifier.Tag = vParametro.identifier
        End If

        If (txtParam.Text <> vParametro.param) Then
            txtParam.Text = vParametro.param
        End If

        If (comboModifier.Text <> vParametro.modifier) Then
            comboModifier.Text = vParametro.modifier
        End If

        updating = False

    End Sub

    Function hasChanged() As Boolean
        If (txtIdentifier.Text <> vParametro.identifier) Then
            Return True
        End If

        If (txtParam.Text <> vParametro.param) Then
            Return True
        End If

        If (comboModifier.Text <> vParametro.modifier) Then
            Return True
        End If
        Return False
    End Function

    Public Function idealWidth()
        ' txtIdentifier.Width = tamanhoCampo(txtIdentifier.Text.Length)
        'txtParam.Width = tamanhoCampo(txtParam.Text.Length)
        Return 0 'txtIdentifier.Width + txtParam.Width + comboModifier.Width
    End Function

    Function tamanhoCampo(ByVal qtdCaracteres)
        Return qtdCaracteres * 25 / 3 + 5
    End Function

    Private Sub txtParam_TextChanged(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles txtParam.TextChanged, txtIdentifier.TextChanged
        If Not vBlueprintController Is Nothing Then
            '     vBlueprintController.updateSize()
        End If
    End Sub


    Sub atualiza()
        If (updating) Then
            Return
        End If
        If (blueprint Is Nothing) Then
            Return
        End If
        If (Not hasChanged()) Then
            Return
        End If

        If (txtIdentifier.Tag <> txtIdentifier.Text) Then
            blueprint.removeParam(vParamList.listName, txtIdentifier.Tag)
        End If

        blueprint.setParam(vParamList.listName, txtIdentifier.Text, comboModifier.Text, txtParam.Text, "")
        txtIdentifier.Tag = txtIdentifier.Text
        vBlueprintController.updateFromModel()
    End Sub

    Private Sub txtIdentifier_Validated(sender As System.Object, e As System.EventArgs) Handles txtParam.Validated, txtIdentifier.Validated, comboModifier.Validated
        atualiza()
    End Sub
End Class
