Public Class ParamListController
    Dim vParamList As ParamList
    Dim itemHeight As Integer = 17
    Dim flagAtivo As Boolean = True
    Dim contaFilho As Integer = 0
    Dim vBlueprintController As BluePrintController
    Dim blueprint As Blueprint

    Public Sub New(ByVal pl As ParamList, ByVal blueprintController As BluePrintController, bp As Blueprint)
        InitializeComponent()
        Me.vBlueprintController = blueprintController
        Me.vParamList = pl
        blueprint = bp

        updateFromModel()
    End Sub

    Sub updateFromModel()
        lblListName.Text = vParamList.listName
        contaFilho = 0
        containerFilho.Controls.Clear()
        Dim par As Parametro
        For Each par In vParamList.params
            Console.WriteLine("Criando parameterEditor para " & par.identifier)
            Dim paramList As parameterEditor = New parameterEditor(par, vBlueprintController, blueprint, vParamList)
            containerFilho.Controls.Add(paramList)
            paramList.Dock = DockStyle.Top
            contaFilho = contaFilho + 1
        Next

        updateSize()
    End Sub

    Sub updateSize()
        Height = idealHeight()
        'Width = idealWidth()
    End Sub

    Public Function idealHeight()
        If Not flagAtivo Then Return itemHeight
        Return itemHeight * (contaFilho + 2) + 8
    End Function

    Public Function idealWidth()
        Dim minWidth As Integer = lblListName.Width + lblListName.Left
        If Not flagAtivo Then Return minWidth

        Dim par As parameterEditor
        For Each par In containerFilho.Controls
            Dim w As Integer = par.idealWidth()
            If (w > minWidth) Then minWidth = w
        Next
        Return minWidth
    End Function

    Private Sub lblListName_LinkClicked(ByVal sender As System.Object, ByVal e As System.Windows.Forms.LinkLabelLinkClickedEventArgs) Handles lblListName.LinkClicked
        flagAtivo = Not flagAtivo
        updateSize()
        vBlueprintController.updateSize()
        If flagAtivo Then
            lblStatus.Text = "-"
        Else
            lblStatus.Text = "+"
        End If
    End Sub

    Private Sub LinkLabel1_LinkClicked(sender As System.Object, e As System.Windows.Forms.LinkLabelLinkClickedEventArgs) Handles LinkLabel1.LinkClicked
        blueprint.setParam(vParamList.listName, "Novo Param", "=", "valor", "")
        vBlueprintController.updateFromModel()
    End Sub
End Class
