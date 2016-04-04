Public Class Blueprint
    Implements IComparer

    Dim vNome As String
    Dim vExtends As String
    Dim vTipo As String
    Dim vListas As ArrayList = New ArrayList()

    Dim arrChildren As ArrayList = New ArrayList()
    Dim bpPai As Blueprint = Nothing
    Dim vComent As String = ""

    Public Sub New(ByVal nome As String, ByVal extends As String, ByVal tipo As String)
        Me.vNome = nome.Replace(" ", "")
        Me.vExtends = extends
        Me.vTipo = tipo

    End Sub

    Function containsTag(tag As String)
        Dim ret As Boolean = False
        Dim tags As String = " " & getValueH("domain", "type") & " "
        If tags.Contains(" " & tag & " ") Then ret = True
        '@domain type+={aicomponent ready}
        Return ret
    End Function

    Function getValueH(lista As String, ident As String)
        Dim ret As String = ""
        Dim pl As ParamList = getListaFor(lista)
        Dim param As Parametro = pl.getParam(ident)
        Dim buscaH As Boolean = True
        If (Not param Is Nothing) Then
            ret = param.param.Replace("{", "").Replace("}", "")
            If param.modifier = "=" Then
                buscaH = False
            End If
        End If
        If Not bpPai Is Nothing And buscaH Then
            ret = ret & " " & bpPai.getValueH(lista, ident)
        End If


        Return ret.Trim
    End Function


    Function getValue(lista As String, ident As String)
        Dim ret As String = ""
        Dim pl As ParamList = getListaFor(lista)
        Dim param As Parametro = pl.getParam(ident)
        If (Not param Is Nothing) Then
            ret = param.param.Replace("{", "").Replace("}", "")
        End If
        Return ret.Trim
    End Function

    Function getValueAsText(lista As String, ident As String)
        Dim ret As String = deslineariza(getValue(lista, ident))
        If (ret.StartsWith("""")) Then ret = ret.Substring(1)
        If (ret.EndsWith("""")) Then ret = ret.Substring(0, ret.Length - 1)

        Return ret.Trim
    End Function

    Function getChildren() As ArrayList
        Return arrChildren
    End Function
    Public Sub addChild(bp As Blueprint)
        Dim bpN As Blueprint
        For Each bpN In arrChildren
            If bpN.nome = bp.nome Then
                Return
            End If
        Next
        arrChildren.Add(bp)
        arrChildren.Sort(bp)
        bp.setPai(Me)
    End Sub

    Public Sub removeChild(bp As Blueprint)
        arrChildren.Remove(bp)
    End Sub

    Public Sub remove()
        If Not bpPai Is Nothing Then
            bpPai.removeChild(Me)
            bpPai = Nothing
        End If
    End Sub

    Sub clearLists()
        vListas.Clear()
    End Sub


    Sub limpaChildren()
        bpPai = Nothing
        arrChildren.Clear()
    End Sub

    Public Sub setPai(bp As Blueprint)
        bpPai = bp
    End Sub

    Public Function getHierarquia(sep As String) As String
        If (Not bpPai Is Nothing) Then
            Return bpPai.getHierarquia(sep) & sep & bpPai.nome
        End If
        Return ""
    End Function

    Function generate() As String
        Dim saida As String = "@" & tipo & " " & vNome
        If (Not bpPai Is Nothing) Then
            saida = saida & ":" & bpPai.nome
        End If
        If (vComent <> "") Then
            saida = saida & " //" & vComent
        End If
        saida = saida & vbNewLine
        Dim par As ParamList
        ordernaListas()
        For Each par In vListas
            Dim pref As String = vbTab & "@" & par.listName & " "
            saida = par.generate(saida, pref)
        Next
        saida = saida & "@end" & vbNewLine
        saida = corrigeKeyWords(saida)

        Return saida
    End Function

    Sub ordernaListas()
        Dim parI As ParamList
        Dim i
        For i = 0 To vListas.Count - 2
            parI = vListas.Item(i)
            Dim parJ As ParamList
            Dim j
            For j = i + 1 To vListas.Count - 1
                parI = vListas.Item(i)
                parJ = vListas.Item(j)
                If comparaNomeLista(parI.listName, parJ.listName) Then
                    vListas.Item(j) = parI
                    vListas.Item(i) = parJ
                End If

            Next

        Next
    End Sub

    Function comparaNomeLista(n1 As String, n2 As String) As Boolean
        Dim r As Integer = checaAmbos(n1, n2, "domain")
        If (r = 1) Then Return True
        If (r = 2) Then Return False
        r = checaAmbos(n1, n2, "property")
        If (r = 1) Then Return True
        If (r = 2) Then Return False
        r = checaAmbos(n1, n2, "entity")
        If (r = 1) Then Return True
        If (r = 2) Then Return False
        r = checaAmbos(n1, n2, "object")
        If (r = 1) Then Return True
        If (r = 2) Then Return False
        r = checaAmbos(n1, n2, "replicate")
        If (r = 1) Then Return True
        If (r = 2) Then Return False
        r = checaAmbos(n1, n2, "childproperty")
        If (r = 1) Then Return True
        If (r = 2) Then Return False

        r = checaAmbos(n1, n2, "comment")
        If (r = 2) Then Return True
        If (r = 1) Then Return False

        Return n1 > n2
    End Function

    Function checaAmbos(n1 As String, n2 As String, lista As String) As Integer
        If (n2 = lista) Then Return 1
        If (n1 = lista) Then Return 2
        Return 0
    End Function


    Function corrigeKeyWords(saida As String)
        saida = saida.Replace("pickone", "pickOne")
        saida = saida.Replace("containstag", "containsTag")
        saida = saida.Replace("isnotnull", "isNotNull")
        saida = saida.Replace("isequals", "isEquals")
        saida = saida.Replace("isnull", "isNull")
        saida = saida.Replace("istrue", "isTrue")
        Return saida
    End Function


    Function size() As Integer
        Return arrChildren.Count
    End Function

    Public Sub setParam(ByVal lista, ByVal ident, ByVal modifier, ByVal param, ByVal coment)
        Dim pl As ParamList = getListaFor(lista)
        pl.addParam(ident, modifier, param, coment)

    End Sub

    Public Sub setParamAsText(ByVal lista, ByVal ident, ByVal modifier, ByVal param)
        setParam(lista, ident, modifier, """" & lineariza(param) & """", "")
    End Sub

    Public Function lineariza(txt As String) As String
        txt = txt.Replace(Chr(13) + Chr(10), "#$#%")
        Return txt
    End Function

    Public Function deslineariza(txt As String) As String
        txt = txt.Replace("#$#%", vbNewLine)
        Return txt
    End Function

    Public Sub removeParam(lista, ident)
        Dim pl As ParamList = getListaFor(lista)
        pl.removeParam(ident)
    End Sub


    Public Function getListaFor(ByVal lista As String) As ParamList
        Dim par As ParamList
        For Each par In vListas
            If (par.listName = lista) Then
                Return par
            End If
        Next

        Dim pl = New ParamList(lista)
        vListas.Add(pl)
        Return pl
    End Function

    Public Property nome() As String
        Get
            Return vNome
        End Get
        Set(ByVal value As String)
            vNome = value
        End Set
    End Property

    Public Property extends() As String
        Get
            Return vExtends
        End Get
        Set(ByVal value As String)
            vExtends = value
        End Set
    End Property

    Public Property tipo() As String
        Get
            Return vTipo
        End Get
        Set(ByVal value As String)
            vTipo = value
        End Set
    End Property


    Public Property coment() As String
        Get
            Return vComent
        End Get
        Set(ByVal value As String)
            vComent = value
        End Set
    End Property

    Public ReadOnly Property listas() As ArrayList
        Get
            Return vListas
        End Get
    End Property

    Public Function Compare(x As Object, y As Object) As Integer Implements System.Collections.IComparer.Compare
        Return x.nome.ToString.ToUpper < y.nome.ToString.ToUpper
    End Function
End Class
