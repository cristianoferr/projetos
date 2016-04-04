
Imports System.Data.OleDb
Imports System.Data.Odbc
Imports System.Data.SqlClient
Imports System.Data.Common
Imports Microsoft.VisualBasic
Imports System.Net

Imports System.IO
Imports System.Security.AccessControl
Imports System.Security.Principal
Imports System.Runtime.InteropServices
Imports System.Net.NetworkInformation
Imports System.Management
Imports System.Xml
Imports System.Text
Imports System

'Public Sub enableDebug()
'Public Sub disableDebug()
'Public Function isDebugging() As Boolean
'Public Sub setConfigFile(ByVal s As String)
'Public Function getParams() As ArrayList
'Public Sub write2File(ByVal file As String, ByVal texto As String, ByVal append As Boolean)
'Public Sub addLog(ByVal file As String, ByVal texto As String)
'Public Function getOwnerofFile(ByVal arq As String) As String
'Function RemoveAcentos(ByVal Texto)
'Public Sub setConnOleDB(ByVal c As OleDbConnection)
'Sub setWorkDir(ByVal wd As String)
'Public Function getEncrypt(ByVal txt As String, ByVal hash As String) As String
'Public Function geraSenhaHumana() As String
'Public Function Data_Hex_Asc(ByVal Data As String) As String
'Public Function Data_Asc_Hex(ByVal Data As String) As String
'Public Function getEncrypt2HEX(ByVal txt As String, ByVal hash As String) As String
'Public Function getEncryptFromHEX2HEX(ByVal txt As String, ByVal hash As String) As String
'Public Function getEncryptFromHEX(ByVal txt As String, ByVal hash As String) As String
'Public Function getPar2(ByVal param As String, ByVal par2 As String) As String
'Public Function getPar2(ByVal param As String, ByVal par2 As String, ByVal arquivo As String) As String
'Public Sub setHash(ByVal h As String)
'Public Function getIntString(ByVal param As String, ByVal lang As String) As String
'Public Function geraPass(ByVal hash As String)
'Public Function getPar(ByVal param As String) As String
'Public Function getPar(ByVal param As String, ByVal arquivo As String) As String
'Public Function getParDireto(ByVal param As String, ByVal arquivo As String) As String
'Public Function getParSimples(ByVal param As String, ByVal arquivo As String) As String
'Public Function getParDiretoSimples(ByVal param As String, ByVal arquivo As String) As String
'Public Sub setPar2(ByVal param As String, ByVal valor As String, ByVal par2 As String)
'Public Sub setPar2(ByVal param As String, ByVal valor As String, ByVal par2 As String, ByVal arquivo As String)
'Public Sub setPar(ByVal param As String, ByVal valor As String)
'Public Sub setPar(ByVal param As String, ByVal valor As String, ByVal arquivo As String)
'Function Tokenize(ByVal TokenString, ByRef TokenSeparators())
'Function pegaPagina(ByVal url As String, ByVal flagProxy As Boolean)
'Function retrieveWebPage(ByVal url As String) As String
'Sub recurseDelete(ByRef strFolder)
'Public Function getNextOleDb(ByVal tabela As String, ByVal campo As String) As Integer
'Public Function getNextOdbc(ByVal tabela As String, ByVal campo As String) As Integer
'Public Function getNext(ByVal tabela As String, ByVal campo As String) As Integer
'Public Function executarComandoCC(ByVal comando As String)
'Public Sub executarComando(ByVal comando As String)
'Public Function FileExists(ByVal Fname As String) As Boolean
'Function verificaHora(ByVal hr As Integer) As Boolean
'Function verificaDiaSemana(ByVal c As Integer) As Boolean
'Public Sub ExecuteSQL(ByVal sql As String)
'Sub copiaArquivos(ByVal origem As String, ByVal destino As String)
'Sub killProcess(ByVal processName)
'Function contaProcesso(ByVal processName)
'Public Function getCurrDir() As String

Public Class util
    Dim workDir As String
    Dim clearcaseDir As String
    Dim connOleDB As OleDbConnection = Nothing
    Dim connODBC As OdbcConnection = Nothing
    Dim params As New ArrayList()
    Public hashInterno As String = ""
    Dim configFile As String = "config.ini"

    Public debugLOG As String = "" 'Essa variavel vai ter os dados de debug e ser· "despejada" sempre que um log acontecer


    Dim flagDebug As String = "" 'vazio=n„o h· debug / arquivo = debug vai para esse arquivo


    Sub New(ByVal wd As String, ByVal cd As String, ByVal c As OleDbConnection)
        workDir = wd
        clearcaseDir = cd
        connOleDB = c
    End Sub
    Sub New(ByVal wd As String, ByVal cd As String, ByVal c As OdbcConnection)
        workDir = wd
        clearcaseDir = cd
        connODBC = c
    End Sub
    Sub New(ByVal wd As String, ByVal configF As String)
        workDir = wd
        configFile = configF
        clearcaseDir = getPar("ccDir")
    End Sub

    Public Sub setConnOleDB(ByVal c As OleDbConnection)
        connOleDB = c
    End Sub

    Public Sub setConnOdbc(ByVal c As OdbcConnection)
        connODBC = c
    End Sub

    Public Sub enableDebug(ByVal arq As String)
        flagDebug = arq
    End Sub
    Public Sub disableDebug()
        flagDebug = ""
    End Sub

    Public Function isDebugging() As String
        Return flagDebug
    End Function

    Public Sub setConfigFile(ByVal s As String)
        configFile = s
    End Sub

    Public Function getParams() As ArrayList
        Return params
    End Function



    ' Main routine to Dimension variables, retrieve user name
    ' and display answer.
    Public Function getUserName() As String

        Return Environ("USERNAME")
    End Function

    'Escreve em um arquivo o texto passado por parametro
    Public Sub write2File(ByVal file As String, ByVal texto As String, ByVal append As Boolean)
        Dim objStreamWriter As StreamWriter
        objStreamWriter = New StreamWriter(file, append)
        objStreamWriter.WriteLine(texto)
        objStreamWriter.Close()
    End Sub

    'Escreve um texto em um arquivo de log com a data e hora do evento
    Public Sub addLog(ByVal file As String, ByVal texto As String)
        write2File(file, Now & " - " & texto, True)
    End Sub

    Public Sub addDebug(ByVal texto As String)
        debugLOG = debugLOG & Now & " - " & texto & Chr(13) & Chr(10)

        If (flagDebug = "") Then
            Return
        End If

        write2File(flagDebug, Now & " - " & texto, True)
    End Sub

    'pega o owner/dono do arquivo
    Public Function getOwnerofFile(ByVal arq As String) As String
        Try
            getOwnerofFile = File.GetAccessControl(arq).GetOwner(GetType(NTAccount)).Value
        Catch ex As Exception
            getOwnerofFile = "ERRO"
        End Try
    End Function


    Function removeAspa(ByVal texto As String, ByVal aspa As String) As String
        If (texto(0) = aspa And texto(texto.Length - 1) = aspa) Then
            texto = texto.Substring(1)
            texto = texto.Substring(0, texto.Length - 1)
        End If
        Return texto

    End Function

    'exemplo ("StringComAspas") vira (StringComAspas)
    Public Function removeAspas(ByVal texto As String) As String
        texto = removeAspa(texto, """")
        texto = removeAspa(texto, "'")
        texto = removeAspa(texto, "¥")
        texto = removeAspa(texto, "`")
        Return texto

    End Function

    '-----------------------------------------------------
    'Funcao:    RemoveAcentos(ByVal Texto)
    'Sinopse:    Remove todos os acentos do texto
    'Parametro: Texto: Texto a ser transformado
    'Retorno: String
    'Autor: Gabriel FrÛes - www.codigofonte.com.br
    '-----------------------------------------------------
    Function RemoveAcentos(ByVal Texto)
        Dim ComAcentos
        Dim SemAcentos
        Dim Resultado
        Dim Cont
        'Conjunto de Caracteres com acentos
        ComAcentos = "¡Õ”⁄…ƒœ÷‹À¿Ã“Ÿ»√’¬Œ‘€ ·ÌÛ˙È‰Ôˆ¸Î‡ÏÚ˘Ë„ı‚ÓÙ˚Í«Á"
        'Conjunto de Caracteres sem acentos
        SemAcentos = "AIOUEAIOUEAIOUEAOAIOUEaioueaioueaioueaoaioueCc"
        Cont = 0
        Resultado = Texto
        Do While Cont < Len(ComAcentos)
            Cont = Cont + 1
            Resultado = Replace(Resultado, Mid(ComAcentos, Cont, 1), Mid(SemAcentos, Cont, 1))
        Loop
        Resultado = Replace(Resultado, "`", "'")
        Resultado = Replace(Resultado, "¥", "'")

        RemoveAcentos = Resultado
    End Function

    



    '    Sub New(ByVal wd As String, ByVal cd As String, ByVal c As OleDbConnection)
    '       workDir = wd
    '      clearcaseDir = cd
    '      conn = c
    ' End Sub
    Sub New()

    End Sub
    Sub setWorkDir(ByVal wd As String)
        workDir = wd
    End Sub

    'FunÁ„o simples para encriptar um texto passando uma hash como base
    Public Function getEncrypt(ByVal txt As String, ByVal hash As String) As String
        Dim ret As String = ""
        Dim c = 0

        Dim soma = 0
        While c < hash.Length
            soma = soma + Asc(hash(c))
            c = c + 1
        End While
        soma = soma - (hash.Length * (soma \ hash.Length))


        c = 0
        While c < txt.Length
            Dim m = c - (hash.Length * (c \ hash.Length))
            ret = ret & Chr(Asc(txt(c)) Xor Asc(Asc(hash(m)) Xor hash.Length) Xor (soma Xor Asc(hash(hash.Length - m - 1))))

            c = c + 1
        End While

        Return ret
    End Function

    'FunÁ„o simples para encriptar um texto passando uma hash como base
    'qtdBytes:qtdBytes que j· foi enviado... usado para poder encriptar arquivos grandes em lote
    Public Function getEncryptLote(ByVal qtdBytes As Int64, ByVal txt As String, ByVal hash As String) As String
        Dim ret As String = ""
        Dim c = 0

        Dim soma = 0
        While c < hash.Length
            soma = soma + Asc(hash(c))
            c = c + 1
        End While
        soma = soma - (hash.Length * (soma \ hash.Length))


        c = 0
        While c < txt.Length
            Dim m = (qtdBytes + c) - (hash.Length * ((qtdBytes + c) \ hash.Length))
            ret = ret & Chr(Asc(txt(c)) Xor Asc(Asc(hash(m)) Xor hash.Length) Xor (soma Xor Asc(hash(hash.Length - m - 1))))

            c = c + 1
        End While

        Return ret
    End Function

    Public Function getEncryptArray(ByVal qtdBytes As Int64, ByVal txt() As Byte, ByVal hash As String) As Byte()
        Dim ret(txt.Length) As Byte

        Dim c = 0

        Dim soma = 0
        While c < hash.Length
            soma = soma + Asc(hash(c))
            c = c + 1
        End While
        soma = soma - (hash.Length * (soma \ hash.Length))


        c = 0
        While c < txt.Length
            Dim m = (qtdBytes + c) - (hash.Length * ((qtdBytes + c) \ hash.Length))
            ret(c) = txt(c) Xor Asc(Asc(hash(m)) Xor hash.Length) Xor (soma Xor Asc(hash(hash.Length - m - 1)))

            c = c + 1
        End While

        Return ret
    End Function


    Public Function geraSenhaHumana() As String
        Randomize()
        Dim i As Integer = 1
        Dim tot = 15
        Dim a(tot) As String
        a(0) = "marlin"
        a(1) = "agulha"
        a(2) = "albacora"
        a(3) = "anequim"
        a(4) = "atum"
        a(5) = "badejo"
        a(6) = "sardinha"
        a(7) = "cavala"
        a(8) = "dourado"
        a(9) = "badejo"
        a(10) = "espada"
        a(11) = "garoupa"
        a(12) = "robalo"
        a(13) = "tubarao"
        a(14) = "salmao"
        a(15) = "molusco"
        i = Rnd() * tot
        Dim senha As String = a(i)
        'i = Rnd() * Len(senha)
        'If (i <= 1) Then i = 2
        'If (i >= Len(senha) - 1) Then i = Len(senha) - 2
        'senha = senha.Substring(0, i) & senha.Substring(i).ToUpper
        i = 0
        While i < 4
            i = i + 1
            Dim d As Integer
            d = Rnd() * 9
            senha = senha & d
        End While





        Return senha

    End Function

    Public Function Data_Hex_Asc(ByVal Data As String) As String
        Dim Data1 = ""
        Dim sData = ""
        While (Data.Length > 0)


            'first take two hex value using substring.
            'then convert Hex value into ascii.
            'then convert ascii value into character.
            Data1 = System.Convert.ToChar(System.Convert.ToUInt32(Data.Substring(0, 2), 16)).ToString()
            sData = sData + Data1
            Data = Data.Substring(2, Data.Length - 2)
        End While

        Return sData
    End Function

    Public Function Data_Asc_Hex(ByVal Data As String) As String

        'first take each charcter using substring.
        'then convert character into ascii.
        'then convert ascii value into Hex Format
        Dim sValue As String
        Dim sHex As String = ""
        Dim c = 0

        While (c < Data.Length)

            sValue = Conversion.Hex(Strings.Asc(Data(c)))
            If sValue.Length = 1 Then sValue = "0" & sValue
            sHex = sHex + sValue
            c = c + 1
        End While

        Return sHex
    End Function

    Public Function getEncrypt2HEX(ByVal txt As String, ByVal hash As String) As String
        Return Data_Asc_Hex(getEncrypt(txt, hash))
    End Function

    Public Function getEncryptFromHEX2HEX(ByVal txt As String, ByVal hash As String) As String
        Return Data_Asc_Hex(getEncrypt(Data_Asc_Hex(txt), hash))
    End Function
    Public Function getEncryptFromHEX(ByVal txt As String, ByVal hash As String) As String
        Return getEncrypt(Data_Hex_Asc(txt), hash)
    End Function

    Public Function getPar2(ByVal param As String, ByVal par2 As String) As String
        Return getEncrypt(Data_Hex_Asc(getPar(Data_Asc_Hex(getEncrypt(param, par2)))), par2)
    End Function

    Public Function getPar2(ByVal param As String, ByVal par2 As String, ByVal arquivo As String) As String
        Return getEncrypt(Data_Hex_Asc(getPar(Data_Asc_Hex(getEncrypt(param, par2)), arquivo)), par2)
    End Function

    Public Sub setHash(ByVal h As String)
        hashInterno = h
    End Sub

    Public Sub enableHash()
        'ESSE VALORES N√O DEVEM SER MUDADOS EM HIPOTESE ALGUMA!904850918590231485908
        setHash("904850918590231485908")
    End Sub

    Public Function spacefy(ByVal texto As String, ByVal tamanho As Integer)
        While texto.Length < tamanho
            texto = texto & " "
        End While
        Return texto
    End Function


    Public Function verificaWildCard(ByVal pattern As String, ByVal input As String) As Boolean
        If Trim(pattern) = "" Then Return True
        Dim arr As Array = pattern.Split(";")
        Dim i As Integer
        Dim ret As Boolean = False
        For i = 0 To UBound(arr)
            If (MatchWildcardString(arr(i), input)) Then ret = True
        Next
        Return ret
    End Function



    Private Function MatchWildcardString(ByVal pattern As String, ByVal input As String) As Boolean
        pattern = pattern.ToUpper
        input = input.ToUpper
        If (String.Compare(pattern, input) = 0) Then
            Return True
        End If
        If (String.IsNullOrEmpty(input)) Then
            If (String.IsNullOrEmpty(pattern.Trim("*"))) Then
                Return True
            Else
                Return False
            End If
        End If
        If (pattern.Length = 0) Then
            Return False
        End If
        If (pattern(0) = "?") Then
            Return MatchWildcardString(pattern.Substring(1), input.Substring(1))
        End If
        If (pattern(pattern.Length - 1) = "?") Then
            Return MatchWildcardString(pattern.Substring(0, pattern.Length - 1), input.Substring(0, input.Length - 1))
        End If
        If (pattern(0) = "*") Then
            If (MatchWildcardString(pattern.Substring(1), input)) Then
                Return True
            Else
                Return MatchWildcardString(pattern, input.Substring(1))
            End If
            If (pattern(pattern.Length - 1) = "*") Then
                If (MatchWildcardString(pattern.Substring(0, pattern.Length - 1), input)) Then
                    Return True
                Else
                    Return MatchWildcardString(pattern, input.Substring(0, input.Length - 1))
                End If
            End If
        End If

        If (pattern(0) = input(0)) Then
            Return MatchWildcardString(pattern.Substring(1), input.Substring(1))
        End If
        Return False
    End Function

    Public Function getIntString(ByVal param As String, ByVal lang As String) As String
        getIntString = ""
        If lang = "" Then lang = "PT"
        param = LCase(param)

        Dim sLine As String = ""
        params.Clear()
        Dim objReader As New StreamReader(workDir & "\lang-" & lang & ".ini")
        Do
            sLine = objReader.ReadLine()
            If Not sLine Is Nothing Then
                params.Add(sLine)
            End If
        Loop Until sLine Is Nothing
        objReader.Close()

        For Each sLine In params
            'Console.WriteLine(sLine)
            If InStr(LCase(sLine), param + "=") Then
                Dim d As String = Mid(sLine, InStr(sLine, "=") + 1, Len(sLine))
                getIntString = d
            End If
        Next


    End Function


    Public Function geraPass(ByVal hash As String)
        Dim ret As String = ""
        Dim i
        Dim tot = 0
        For i = 0 To hash.Length - 1
            tot = tot + Asc(hash(i))
        Next

        Dim hoje As DateTime = DateTime.UtcNow
        ret = getEncrypt(hash, tot)
        ret = getEncrypt(ret, hash.Length)

        ret = getEncrypt(ret, hashInterno)
        ret = getEncrypt(ret, Day(hoje))
        ret = getEncrypt(ret, Month(hoje))
        ret = getEncrypt2HEX(ret, Year(hoje))

        ret = Chr(97 + Month(hoje)) & ret
        'Dim ret2 As String = ""
        'ret2 = getEncrypt(hash, Day(hoje) + 1)
        'ret2 = getEncrypt(ret2, Month(hoje))
        'ret2 = getEncrypt2HEX(ret2, Year(hoje))

        'Dim d = Data_Hex_Asc(ret)
        'd = getEncrypt(d, Year(Today))
        'd = getEncrypt(d, Month(Today))
        'd = getEncrypt(d, Day(Today))
        geraPass = ret
    End Function

    Public Function getPar(ByVal param As String) As String
        Return getPar(param, configFile)


    End Function

    Public Function getPar(ByVal param As String, ByVal arquivo As String) As String
        Return getParDireto(param, workDir & "\" & arquivo)
    End Function

    Public Function getParDireto(ByVal param As String, ByVal arquivo As String) As String
        Try

            getParDireto = ""
            param = LCase(param)

            Dim sLine As String = ""
            params.Clear()
            Dim objReader As New StreamReader(arquivo)
            Do
                sLine = objReader.ReadLine()
                If Not sLine Is Nothing Then
                    params.Add(LCase(sLine))
                End If
            Loop Until sLine Is Nothing
            objReader.Close()

            For Each sLine In params
                'Console.WriteLine(sLine)
                If hashInterno <> "" Then
                    sLine = LCase(getEncrypt(Data_Hex_Asc(sLine), hashInterno))
                    'param = LCase(Data_Asc_Hex(getEncrypt(param, hashInterno)))
                End If
                If InStr(sLine, param + "=") Then
                    Dim d As String = Mid(sLine, InStr(sLine, "=") + 1, Len(sLine))
                    d = d.Replace("apppath", workDir)
                    getParDireto = d
                End If
            Next

        Catch ex As Exception
            ' se deu erro... retorna vazio
            getParDireto = ""
        End Try

    End Function

    Public Function getParSimples(ByVal param As String, ByVal arquivo As String) As String
        Return getParDiretoSimples(param, workDir & "\" & arquivo)
    End Function

    Public Function getParDiretoSimples(ByVal param As String, ByVal arquivo As String) As String
        getParDiretoSimples = ""
        param = LCase(param)

        Dim sLine As String = ""
        params.Clear()
        Dim objReader As New StreamReader(arquivo)
        Do
            sLine = objReader.ReadLine()
            If Not sLine Is Nothing Then
                params.Add(LCase(sLine))
            End If
        Loop Until sLine Is Nothing
        objReader.Close()

        For Each sLine In params
            'Console.WriteLine(sLine)
            If InStr(sLine, param + "=") Then
                Dim d As String = Mid(sLine, InStr(sLine, "=") + 1, Len(sLine))
                d = d.Replace("apppath", workDir)
                getParDiretoSimples = d
            End If
        Next


    End Function

    Public Sub setPar2(ByVal param As String, ByVal valor As String, ByVal par2 As String)
        setPar(getEncrypt2HEX(param, par2), getEncrypt2HEX(valor, par2))
    End Sub

    Public Sub setPar2(ByVal param As String, ByVal valor As String, ByVal par2 As String, ByVal arquivo As String)
        setPar(getEncrypt2HEX(param, par2), getEncrypt2HEX(valor, par2), arquivo)
    End Sub


    Public Sub setPar(ByVal param As String, ByVal valor As String)
        setPar(param, valor, configFile)
    End Sub


    Public Sub setPar(ByVal param As String, ByVal valor As String, ByVal arquivo As String)
        '        param = LCase(param)

        Dim sLine As String = ""
        params.Clear()
        Dim objReader As New StreamReader(workDir & "\" & arquivo)
        Do
            sLine = objReader.ReadLine()
            If Not sLine Is Nothing Then
                params.Add(LCase(sLine))
            End If
        Loop Until sLine Is Nothing
        objReader.Close()


        Dim objW As New StreamWriter(workDir & "\" & arquivo, False)


        Dim found As Boolean = False
        For Each sLine In params
            'Console.WriteLine(sLine)
            If hashInterno <> "" Then
                Dim orig = sLine
                sLine = getEncrypt(Data_Hex_Asc(sLine), hashInterno)
                'param = LCase(Data_Asc_Hex(getEncrypt(param, hashInterno)))
            End If
            If InStr(sLine, param + "=") Then
                'Dim d As String = Mid(sLine, InStr(sLine, "=") + 1, Len(sLine))
                'd = d.Replace("apppath", workDir)
                '  getPar = d
                If hashInterno <> "" Then
                    objW.WriteLine(getEncrypt2HEX(param & "=" & valor, hashInterno))
                Else
                    objW.WriteLine(param & "=" & valor)
                End If
                found = True
            Else
                If (hashInterno <> "") Then
                    objW.WriteLine(getEncrypt2HEX(sLine, hashInterno))
                Else
                    objW.WriteLine(sLine)
                End If
            End If
        Next
        If Not found Then
            If (hashInterno <> "") Then
                objW.WriteLine(getEncrypt2HEX(param & "=" & valor, hashInterno))
            Else
                objW.WriteLine(param & "=" & valor, hashInterno)
            End If

        End If
            objW.Close()
    End Sub
    Function Tokenize(ByVal TokenString, ByRef TokenSeparators())

        Dim NumWords, a()
        NumWords = 0

        Dim NumSeps
        NumSeps = UBound(TokenSeparators)
        Dim SepIndex, SepPosition, i
        Dim sepToken = ""
        Do

            SepPosition = 0
            SepIndex = -1

            For i = 0 To NumSeps - 1

                ' Find location of separator in the string

                Dim pos
                pos = InStr(TokenString, TokenSeparators(i))

                ' Is the separator present, and is it closest to the beginning of the string?

                If pos > 0 And ((SepPosition = 0) Or (pos < SepPosition)) Then
                    SepPosition = pos
                    SepIndex = i
                    sepToken = TokenSeparators(i)
                End If

            Next

            ' Did we find any separators?	

            If SepIndex < 0 Then

                ' None found - so the token is the remaining string

                ReDim Preserve a(NumWords + 1)
                a(NumWords) = TokenString

            Else

                ' Found a token - pull out the substring		

                Dim substr
                substr = Trim(Left(TokenString, SepPosition - 1))

                ' Add the token to the list

                ReDim Preserve a(NumWords + 2)
                a(NumWords) = substr
                a(NumWords + 1) = sepToken
                NumWords = NumWords + 1

                ' Cutoff the token we just found

                Dim TrimPosition
                TrimPosition = SepPosition + Len(TokenSeparators(SepIndex))
                TokenString = Trim(Mid(TokenString, TrimPosition))

            End If

            NumWords = NumWords + 1
        Loop While (SepIndex >= 0)

        Tokenize = a

    End Function

    Function pegaPagina(ByVal url As String, ByVal flagProxy As Boolean)
        Dim strResp As String = ""
        Try

            Dim sProxy As String = "10.2.108.25:8080"
            Dim xmlhttp = CreateObject("MSXML2.ServerXMLHTTP.4.0")
            If flagProxy Then
                xmlhttp.setProxy(2, sProxy, "")
            End If
            xmlhttp.open("GET", url, False)
            xmlhttp.setRequestHeader("Content-Type", "text/html")
            xmlhttp.setRequestHeader("charset", "ISO-8859-1")

            xmlhttp.send()
            '

            strResp = xmlhttp.responseText


            '   Dim wc As New System.Net.WebClient
            '   Dim oProxy As WebProxy = New WebProxy(sProxy, True)

            '   oProxy.Credentials = New System.Net.NetworkCredential(sChave, sPass)
            '   wc.Proxy = oProxy
            '   wc.Encoding = System.Text.Encoding.UTF8
            '            wc.Encoding.
            '    wc.DownloadFile(url, "c:\temp\arquivo.txt")
            '    Dim objStreamReader As StreamReader
            '    objStreamReader = New StreamReader("c:\temp\arquivo.txt", System.Text.Encoding.UTF8)

            '    Dim st = ""
            '    Do
            ' st = objStreamReader.ReadLine()
            ' If st <> "" Then
            'strResp = strResp & st
            'End If
            '
            '            Loop Until st Is Nothing
            ' objStreamReader.Close()
            ' objStreamReader = Nothing

        Catch ex As Exception
            Console.WriteLine(ex.Message)
        End Try
        pegaPagina = strResp
    End Function
    Function retrieveWebPage(ByVal url As String) As String
        Dim request As WebRequest = WebRequest.Create(url)
        Dim response As WebResponse = request.GetResponse()
        retrieveWebPage = response.ToString

    End Function
    Sub recurseDelete(ByRef strFolder)
        Dim objFSO = CreateObject("Scripting.FileSystemObject")
        Dim objFolders = objFSO.GetFolder(strFolder)

        Dim objSubFolders = objFolders.SubFolders
        Dim objFiles = objFolders.Files
        Dim file
        Dim folder

        For Each file In objFiles
            file.Delete()
        Next

        For Each folder In objSubFolders
            recurseDelete(folder)
            'folder.delete()
        Next


        objSubFolders = Nothing
        objFiles = Nothing
    End Sub

    Public Function getNextOleDb(ByVal tabela As String, ByVal campo As String) As Integer
        Dim r As Integer = 1
        Try
            Dim sql As String = "SELECT max(" & campo & ")+1 as tot FROM " & tabela & " "

            Dim cmd As New OleDbCommand(sql, connOleDB)
            Dim DR As OleDbDataReader = cmd.ExecuteReader(CommandBehavior.Default)
            If DR.Read Then
                If DR.Item("tot").ToString = "" Then
                    r = 1
                Else
                    r = DR.Item("tot").ToString
                End If
            End If
            DR.Close()
            DR.Dispose()
            cmd.Dispose()


            ' 
        Catch ex As Exception
            Console.WriteLine("Erro(getNext): " & ex.Message)
        End Try
        Return r
    End Function
    Public Function getNextOdbc(ByVal tabela As String, ByVal campo As String) As Integer
        Dim r As Integer = 1
        Try
            Dim sql As String = "SELECT max(" & campo & ")+1 as tot FROM " & tabela & " "

            Dim cmd As New OdbcCommand(sql, connODBC)
            Dim DR As OdbcDataReader = cmd.ExecuteReader(CommandBehavior.Default)
            If DR.Read Then
                If DR.Item("tot").ToString = "" Then
                    r = 1
                Else
                    r = DR.Item("tot").ToString
                End If
            End If
            DR.Close()
            DR.Dispose()
            cmd.Dispose()


            ' 
        Catch ex As Exception
            Console.WriteLine("Erro(getNext): " & ex.Message)
        End Try
        Return r
    End Function

    Public Function getValorOdbc(ByVal sql As String, ByVal campo As String) As String
        Try
            Dim txt As String = Nothing
            Dim cmd As New OdbcCommand(sql, connODBC)
            Dim DR As OdbcDataReader = cmd.ExecuteReader(CommandBehavior.Default)
            If DR.Read Then
                txt = DR.Item(campo).ToString
            End If
            DR.Close()
            DR.Dispose()
            cmd.Dispose()
            Return txt

            ' 
        Catch ex As Exception
            Console.WriteLine("Erro(getNext): " & ex.Message)
        End Try
        Return Nothing
    End Function

    Public Function getNext(ByVal tabela As String, ByVal campo As String) As Integer
        If connODBC Is Nothing Then
            getNext = getNextOleDb(tabela, campo)
        Else
            getNext = getNextOdbc(tabela, campo)
        End If
    End Function


    Public Function executarComandoCC(ByVal comando As String)
        Dim objStreamWriter As StreamWriter

        'Pass the file path and the file name to the StreamWriter constructor.
        objStreamWriter = New StreamWriter(workDir + "exec.bat")

        'Write a line of text.
        Randomize()
        Dim rand As Integer = Rnd() * 1000
        Dim file As String = workDir + "saida" & rand & ".tmp"
        objStreamWriter.WriteLine("@echo off")
        objStreamWriter.WriteLine("del " & workDir + "*.tmp")
        objStreamWriter.WriteLine("""" & clearcaseDir + "cleartool.exe"" " & comando & " >" & file)
        'objStreamWriter.WriteLine("pause")
        objStreamWriter.Close()
        'Application.DoEvents()
        ChDir(workDir)
        Shell(workDir + "exec.bat", AppWinStyle.Hide, True)
        Return file
    End Function

    Public Sub executarComando(ByVal comando As String)
        Dim objStreamWriter As StreamWriter

        'Pass the file path and the file name to the StreamWriter constructor.
        objStreamWriter = New StreamWriter(workDir + "exec.bat")

        'Write a line of text.
        Randomize()

        objStreamWriter.WriteLine("@echo off")
        objStreamWriter.WriteLine(comando)
        objStreamWriter.WriteLine("del exec.bat")
        'objStreamWriter.WriteLine("pause")
        objStreamWriter.Close()
        'Application.DoEvents()
        ChDir(workDir)
        Shell(workDir + "exec.bat", AppWinStyle.Hide, False)

    End Sub
    Public Function FileExists(ByVal Fname As String) As Boolean
        FileExists = (Dir(Fname) <> "")
    End Function

    Function verificaHora(ByVal hr As Integer) As Boolean
        Dim h = Hour(Now)
        If hr = h Then
            verificaHora = True
        Else
            verificaHora = False
        End If
    End Function
    Function verificaDiaSemana(ByVal c As Integer) As Boolean
        Dim wd = Weekday(Now)
        verificaDiaSemana = False
        If (wd = 1) And c >= 64 Then
            c = c - 64
            verificaDiaSemana = True
        End If
        If (wd = 2) And c >= 32 Then
            c = c - 32
            verificaDiaSemana = True
        End If
        If (wd = 3) And c >= 16 Then
            c = c - 16
            verificaDiaSemana = True
        End If
        If (wd = 4) And c >= 8 Then
            c = c - 8
            verificaDiaSemana = True
        End If
        If (wd = 5) And c >= 4 Then
            c = c - 4
            verificaDiaSemana = True
        End If
        If (wd = 6) And c >= 2 Then
            c = c - 2
            verificaDiaSemana = True
        End If
        If (wd = 7) And c >= 1 Then
            c = c - 1
            verificaDiaSemana = True
        End If


    End Function

    Public Sub executeSQL(ByVal sql As String)
        If sql <> Nothing Then
            ' If conn.State = ConnectionState.Closed Then
            ' conn.Open()
            'End If
            If connOleDB Is Nothing Then
                Dim cmd As New OdbcCommand(sql, connODBC)
                cmd.ExecuteNonQuery()
                cmd.Dispose()
            Else
                Dim cmd As New OleDbCommand(sql, connOleDB)
                cmd.ExecuteNonQuery()
                cmd.Dispose()
            End If
        End If
    End Sub
    Sub copiaArquivos(ByVal origem As String, ByVal destino As String)
        Console.WriteLine("Copiando arquivo de:" & origem & " para " & destino)
        If My.Computer.FileSystem.FileExists(origem) Then
            My.Computer.FileSystem.CopyFile(origem, destino, True)
        Else
            Console.WriteLine("Arquivo origem n„o existe!")
        End If
    End Sub


    Sub killProcess(ByVal processName)
        ' loop through the running processes and add
        'each to the list
        Dim t As Integer = 0
        Dim p As System.Diagnostics.Process
        For Each p In System.Diagnostics.Process.GetProcesses()
            Dim s = LCase(p.ProcessName)
            If InStr(s, processName) > 0 Then
                p.Kill()
            End If


        Next


    End Sub

    Function contaProcesso(ByVal processName As String, ByVal owner As String)
        ' loop through the running processes and add
        'each to the list
        owner = owner.ToLower

        processName = LCase(processName)
        processName = Replace(processName, ".exe", "")

        Dim t As Integer = 0


        Dim p As System.Diagnostics.Process
        For Each p In System.Diagnostics.Process.GetProcesses()
            Try


                Dim tp = System.Diagnostics.Process.GetProcessById(p.Id)
                Dim user = tp.StartInfo.UserName
                Dim s = LCase(p.ProcessName)
                '            If InStr(s, processName) > 0 Then
                If s = processName Then
                    If owner = "" Or (getProcessOwner(p.Id) = owner) Then
                        t = t + 1
                    End If
                End If
            Catch ex As Exception

            End Try

        Next

        contaProcesso = t
    End Function

    Function getProcessOwner(ByVal PID As Integer) As String
        

        Dim args(1) As Object
        Dim ms As New ManagementObjectSearcher("SELECT * FROM Win32_Process WHERE ProcessId = " & PID)

        For Each mo As ManagementObject In ms.Get
            If CUInt(mo.InvokeMethod("GetOwner", args)) = 0 Then
                'Console.WriteLine(args(1).ToString & ": " & args(0).ToString)
                Return args(0).ToString.ToLower
            End If
        Next
        Return ""

      
    End Function
    Public Function getCurrDir() As String
        Dim objFSO = CreateObject("Scripting.FileSystemObject")
        Dim objFolders = objFSO.GetFolder(".")

        Return objFolders.path
    End Function


End Class

