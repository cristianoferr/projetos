Public Class Parametro
    Dim vIdentifier As String
    Dim vModifier As String
    Dim vParam As String
    Dim vComent As String

    Public Sub New(ByVal ident As String, ByVal modifier As String, ByVal par As String, coment As String)
        Me.vIdentifier = ident
        Me.vModifier = modifier
        par = par.Replace("""", "'")
        par = validaNiveisParam(par, "(", ")")
        par = validaNiveisParam(par, "{", "}")
        par = validaNiveisParam(par, "[", "]")
        par = validaNiveisParam(par, "'", "'")
        Me.vParam = par
        Me.vComent = coment

    End Sub

    Function validaNiveisParam(par As String, open As String, close As String) As String
        Dim countAbre As Integer = 0
        Dim countFecha As Integer = 0
        Dim c As Integer = 0
        While c < par.Length
            If par(c) = open Then countAbre = countAbre + 1
            If par(c) = close Then countFecha = countFecha + 1
            c = c + 1
        End While
        If (countAbre <> countFecha) Then
            par = InputBox("Erro de Niveis:'" & open & "' e '" & close & "' ", "Corrija:", par)
        End If

        Return par
    End Function


    Public Property coment() As String
        Get
            Return vComent
        End Get
        Set(ByVal value As String)
            vComent = value
        End Set
    End Property


    Public Property identifier() As String
        Get
            Return vIdentifier
        End Get
        Set(ByVal value As String)
            vIdentifier = value
        End Set
    End Property

    Public Property modifier() As String
        Get
            Return vModifier
        End Get
        Set(ByVal value As String)
            vModifier = value
        End Set
    End Property

    Public Property param() As String
        Get
            Return vParam
        End Get
        Set(ByVal value As String)
            vParam = value.Replace("""", "'")
        End Set
    End Property
End Class
