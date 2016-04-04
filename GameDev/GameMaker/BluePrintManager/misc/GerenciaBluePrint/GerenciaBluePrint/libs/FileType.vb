Public Class FileType
    Dim vNome As String
    Dim vTamanho As Long
    Dim vData As Date
    Dim vPath As String
    Dim vflagFolder As Boolean

    Public Sub New(ByVal path As String, ByVal nome As String, ByVal tamanho As Long, ByVal data As Date, ByVal flagFolder As Boolean)
        vNome = nome
        vTamanho = tamanho
        vPath = path
        vData = data
        vflagFolder = flagFolder
    End Sub

    Public Sub New(ByVal path As String, ByVal nome As String, ByVal tamanho As Long, ByVal data As String, ByVal flagFolder As Boolean)
        vNome = nome
        vPath = path
        vTamanho = tamanho
        vflagFolder = flagFolder
        Dim c = 0
        If data.Length > 14 Then
            If data(14) = "P" Then c = 12
        End If
        Dim h = (data(9) & data(10)) + c
        If h = 24 Then h = 12
        vData = New Date((data(6) & data(7)) + 2000, data(0) & data(1), data(3) & data(4), h, (data(12) & data(13)), 0)


    End Sub


    Public Property nome() As String
        Get
            Return vNome
        End Get
        Set(ByVal value As String)
            vNome = value
        End Set
    End Property

    Public Property path() As String
        Get
            Return vPath
        End Get
        Set(ByVal value As String)
            vPath = value
        End Set
    End Property

    Public Property isFolder() As Boolean
        Get
            Return vflagFolder
        End Get
        Set(ByVal value As Boolean)
            vflagFolder = value
        End Set
    End Property

    Public Property tamanho() As Long
        Get
            Return vTamanho
        End Get
        Set(ByVal value As Long)
            vTamanho = value
        End Set
    End Property

    Public Property Data() As Date
        Get
            Return vData
        End Get
        Set(ByVal value As Date)
            vData = value
        End Set
    End Property
End Class
