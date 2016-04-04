Public Class Define
    Public name As String
    Public value As String
    Public tipo As String

    Public Sub New(tipo As String, ByVal nome As String, value As String)
        Me.tipo = tipo
        Me.name = nome
        Me.value = value
    End Sub

    Public Function generate() As String
        Return tipo & " " & name & "=" & value
    End Function


End Class
