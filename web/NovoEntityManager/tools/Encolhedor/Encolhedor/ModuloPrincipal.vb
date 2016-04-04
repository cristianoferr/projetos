Imports System.Threading
Imports System.Data.OleDb
Imports System.Data.SqlClient
Imports System.Data.Common
Imports Microsoft.VisualBasic

Imports System.IO
Imports System.Security.AccessControl
Imports System.Security.Principal
Imports System.Management
Imports System.Runtime.InteropServices
Imports System.Net.NetworkInformation

Imports System.Xml
Imports System.Text
Imports System
Imports System.Net

Module ModuloPrincipal

    Dim util As util
    Dim workDir As String
    Public local As LocalClient = Nothing

    Dim rootDir As String
    Dim modulosDir As String
    Dim buildDir As String
    Dim fnrPath As String
    Dim buildDirAbsolute As String
    Dim loaderFile As String

    Dim analiseCSS As IAnalise
    Dim analiseJS As IAnalise
    Dim analisePHP As IAnalise
    Dim sufixo As String = "-inc"

    Sub Main()
        Console.WriteLine("rodando...")
        local = New LocalClient()
        workDir = local.getCurrDir
        util = New util(workDir, "config.ini")

        rootDir = util.getPar("raiz_projeto")
        modulosDir = util.getPar("modulo_dir")
        loaderFile = util.getPar("loader_file")
        buildDir = rootDir & util.getPar("build_dest")
        fnrPath = util.getPar("fnrpath")
        buildDirAbsolute = util.getPar("build_dest_absolute")

        analiseCSS = New AnaliseCSS
        analiseJS = New AnaliseJS
        analisePHP = New AnalisePHP

        local.deleteFile(buildDir & "\minified.css" & sufixo)
        local.deleteFile(buildDir & "\minified.js" & sufixo)
        local.deleteFile(buildDir & "\minified.php" & sufixo)

        listaModulos()
       

        analiseCSS.report()
        analiseJS.report()
        analisePHP.report()

        finalizaPHP("minified.php" & sufixo)
        Console.WriteLine("Minificando...")
        minifica()

        Console.WriteLine("fim")

        Console.ReadKey()

    End Sub

    Sub listaModulos()
        Dim objReader As New StreamReader(rootDir & modulosDir & "\order.txt")
        Dim sLine As String = ""
        Do
            sLine = objReader.ReadLine()
            If Not sLine Is Nothing Then
                Console.WriteLine("Analisando módulo: " & sLine)
                comprimeModulo(sLine)
            End If
        Loop Until sLine Is Nothing
    End Sub

    Sub finalizaPHP(arquivo)
        Dim objW As New StreamWriter(buildDir & "\" & arquivo, True)
        objW.Write("END-OF-FILE")
        objW.Close()
    End Sub

    Sub comprimeModulo(nome As String)
        Dim m = New Modulo(nome, rootDir & modulosDir & "\" & nome, loaderFile, buildDir)
        m.setAnalises(analiseCSS, analiseJS, analisePHP)
        m.setSufixo(sufixo)
        m.comprimeModulo()
    End Sub

    Sub minificaArquivo(origem As String, destino As String, tipo As String)
        Dim yuipath = util.getPar("yuipath")
        Dim cmd As String = "java -jar " & yuipath & " --type " & tipo & " " & buildDir & "\" & origem & " -o " & buildDir & "\" & destino
        Console.WriteLine(cmd)
        Shell(cmd)
    End Sub

    Sub minifica()
        minificaArquivo("minified.js" & sufixo, "final-min.js" & sufixo, "js")
        minificaArquivo("minified.css" & sufixo, "final-min.css" & sufixo, "css")

        simplificaPHP("<?php", "<?")
        simplificaPHP("?> \n<?", "")
        simplificaPHP("?>\n<?", "")
        simplificaPHP("\n}\n", "}")
        simplificaPHP("\n} \n", "}")
        simplificaPHP("?>\nEND-OF-FILE", "?>")



    End Sub
    Sub simplificaPHP(findStr, replaceStr)
        findStr = """" & findStr & """"
        replaceStr = """" & replaceStr & """"
        Dim cmd As String = fnrPath & " --cl --find " & findStr & " --replace " & replaceStr & " --fileMask ""minified.php*"" --dir """ & buildDirAbsolute & """"
        Console.WriteLine(cmd)
        Shell(cmd, AppWinStyle.Hide, True)
        Thread.Sleep(1000)
    End Sub

End Module
