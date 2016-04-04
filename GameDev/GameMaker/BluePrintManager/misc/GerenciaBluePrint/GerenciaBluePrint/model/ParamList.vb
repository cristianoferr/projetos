Public Class ParamList
    Dim vListName As String
    Dim vParams As ArrayList = New ArrayList()


    Public Sub New(ByVal listName As String)
        Me.vListName = listName
    End Sub

    Public Function generate(saida As String, pref As String) As String
        Dim par As Parametro
        For Each par In vParams
            Dim coment As String = ""
            If (par.coment <> "") Then
                coment = " //" & par.coment.Trim

            End If
            saida = saida & pref & par.identifier & par.modifier & par.param & coment & vbNewLine
        Next
        Return saida
    End Function

    Public Sub removeParam(ident As String)
        removeIfExists(ident)
    End Sub

    Public Sub addParam(ByVal ident As String, ByVal modifier As String, ByVal param As String, coment As String)
        removeIfExists(ident)
        If (listName = "property" And ident = "identifier") Then
            Return
        End If
        Dim par As Parametro = New Parametro(ident, modifier, param, coment)
        vParams.Add(par)
    End Sub

    Private Sub removeIfExists(ByVal ident As String)
        Dim par As Parametro = getParam(ident)
        If (Not par Is Nothing) Then
            params.Remove(par)
        End If
    End Sub

    Public Function getParam(ByVal ident As String) As Parametro
        Dim par As Parametro
        For Each par In vParams
            If (par.identifier = ident) Then
                Return par
            End If
        Next
        Return Nothing
    End Function


    Public ReadOnly Property listName() As String
        Get
            Return vListName
        End Get
    End Property

    Public ReadOnly Property params() As ArrayList
        Get
            Return vParams
        End Get
    End Property
End Class
