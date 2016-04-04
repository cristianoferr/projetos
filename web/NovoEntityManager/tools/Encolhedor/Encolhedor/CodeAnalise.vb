Public Interface IAnalise
    Sub iniciaNovoArquivo()
    Sub adicionaLinha(linha As String)
    Sub fimArquivo(nomeArquivo As String)

    Sub report()
End Interface

Public Class AnaliseBase
    Protected primeiraLinha As String
    Protected ultimaLinha As String
    Protected qtdArquivos = 0
    Protected qtdLinhas = 0

    Sub novoArquivo()
        primeiraLinha = ""
        ultimaLinha = ""
        qtdArquivos = qtdArquivos + 1
    End Sub

    Sub novaLinha(linha As String)
        If primeiraLinha = "" Then
            primeiraLinha = linha
        End If
        ultimaLinha = linha
        qtdLinhas = qtdLinhas + 1
    End Sub

    Sub erroArquivo(nomeArquivo As String, erro As String)
        Console.WriteLine("ERRO em " & nomeArquivo & ": " & erro)
    End Sub

    Sub estatisticasGerais(tipo As String)
        Console.WriteLine("-------------------------")
        Console.WriteLine("Estatisticas para " & tipo)
        Console.WriteLine("Arquivos: " & qtdArquivos)
        Console.WriteLine("Linhas: " & qtdLinhas)
        Console.WriteLine("Linha/Arquivo: " & Math.Round(qtdLinhas / qtdArquivos))
    End Sub

End Class

Public Class AnaliseCSS
    Inherits AnaliseBase
    Implements IAnalise


    Sub iniciaNovoArquivo() Implements IAnalise.iniciaNovoArquivo
        novoArquivo()
    End Sub

    Sub adicionaLinha(linha As String) Implements IAnalise.adicionaLinha
        novaLinha(linha)


    End Sub

    Sub fimArquivo(nomeArquivo As String) Implements IAnalise.fimArquivo

    End Sub

    

    Public Sub report() Implements IAnalise.report
        estatisticasGerais("CSS")
    End Sub
End Class

Public Class AnaliseJS
    Inherits AnaliseBase
    Implements IAnalise


    Sub iniciaNovoArquivo() Implements IAnalise.iniciaNovoArquivo
        novoArquivo()
    End Sub

    Sub adicionaLinha(linha As String) Implements IAnalise.adicionaLinha
        novaLinha(linha)
    End Sub

    Sub fimArquivo(nomeArquivo As String) Implements IAnalise.fimArquivo

    End Sub


    Public Sub report() Implements IAnalise.report
        estatisticasGerais("JS")
    End Sub
End Class



Public Class AnalisePHP
    Inherits AnaliseBase
    Implements IAnalise
    Dim contaFuncoes As Integer = 0
    Dim contaClasses As Integer = 0
    Dim contaInterfaces As Integer = 0
    Dim contaFuncoesCobertas As Integer = 0
    Dim linhaEmFuncoes As Integer = 0
    Dim dentroFuncao As Boolean = False
    Dim dentroInterface As Boolean = False

    Sub iniciaNovoArquivo() Implements IAnalise.iniciaNovoArquivo
        novoArquivo()
        dentroInterface = False
    End Sub

    Sub contaFuncao(linha As String)
        If (linha.Contains("function ")) Then
            contaFuncoes = contaFuncoes + 1
            If (linha.Contains("//coberto")) Then
                contaFuncoesCobertas = contaFuncoesCobertas + 1
            End If
            dentroFuncao = True
        Else
            If dentroFuncao Then
                linhaEmFuncoes = linhaEmFuncoes + 1
            End If
        End If
    End Sub

    Sub adicionaLinha(linha As String) Implements IAnalise.adicionaLinha
        linha = linha.ToLower
        If (linha.Contains("interface ")) Then
            dentroInterface = True
            contaInterfaces = contaInterfaces + 1
        End If
        If (linha.Contains("class ")) Then
            dentroInterface = False
            contaClasses = contaClasses + 1
        End If

        If Not dentroInterface Then
            contaFuncao(linha)
        End If

        novaLinha(linha)
    End Sub

    Sub fimArquivo(nomeArquivo As String) Implements IAnalise.fimArquivo
        If Not primeiraLinha.StartsWith("<?") Then
            erroArquivo(nomeArquivo, "não começa com <?")
        End If
        If Not ultimaLinha.EndsWith("?>") Then
            erroArquivo(nomeArquivo, "não termina com ?>: '" & ultimaLinha & "' ")
        End If
        dentroFuncao = False
    End Sub

    Public Sub report() Implements IAnalise.report
        estatisticasGerais("PHP")
        Console.WriteLine("Funcoes: " & contaFuncoes & " Cobertas: " & contaFuncoesCobertas & " Relação: " & Math.Round(contaFuncoesCobertas / contaFuncoes * 100) & "% ")
        Console.WriteLine("Linhas em Funcoes: " & linhaEmFuncoes & " Linha/Funcao médio: " & Math.Round(linhaEmFuncoes / contaFuncoes))
        Console.WriteLine("Interfaces: " & contaInterfaces & " Classes: "& contaClasses)
    End Sub
End Class

