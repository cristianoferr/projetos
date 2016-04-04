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

Imports System.Xml
Imports System.Text
Imports System


Public Class LocalClient


    Private Declare Function ShellExecute Lib "shell32.dll" Alias _
          "ShellExecuteA" (ByVal hwnd As Long, ByVal lpszOp As _
          String, ByVal lpszFile As String, ByVal lpszParams As String, _
          ByVal lpszDir As String, ByVal FsShowCmd As Long) As Long

    Private Declare Function GetDesktopWindow Lib "user32" () As Long

    Const SW_SHOWNORMAL = 1

    Const SE_ERR_FNF = 2&
    Const SE_ERR_PNF = 3&
    Const SE_ERR_ACCESSDENIED = 5&
    Const SE_ERR_OOM = 8&
    Const SE_ERR_DLLNOTFOUND = 32&
    Const SE_ERR_SHARE = 26&
    Const SE_ERR_ASSOCINCOMPLETE = 27&
    Const SE_ERR_DDETIMEOUT = 28&
    Const SE_ERR_DDEFAIL = 29&
    Const SE_ERR_DDEBUSY = 30&
    Const SE_ERR_NOASSOC = 31&
    Const ERROR_BAD_FORMAT = 11&


    Public Sub mudaDir(ByVal path As String)
        ChDir(path)
    End Sub

    Public Function getDataModificacao(fname As String) As Date
        Return IO.File.GetLastWriteTime(fname)
    End Function

    Public Function FileExists(ByVal Fname As String) As Boolean
        FileExists = (Dir(Fname) <> "") Or (Dir(Fname & "\") <> "")
    End Function

    Public Function FolderExists(ByVal Fname As String) As Boolean
        Dim caminhoCompleto As String = getCurrDir() & "\" & Fname
        'FolderExists = ((Dir(Fname & "\") <> "") Or (Dir(caminhoCompleto & "\") <> ""))
        Dim b = My.Computer.FileSystem.DirectoryExists(caminhoCompleto)
        FolderExists = My.Computer.FileSystem.DirectoryExists(Fname)
    End Function

    Public Sub deleteFile(ByVal arquivo As String)
        Try
            My.Computer.FileSystem.DeleteFile(arquivo)
        Catch ex As Exception

        End Try

    End Sub


    Public Sub deleteFolder(ByVal arquivo As String)
        Try
            My.Computer.FileSystem.DeleteDirectory(arquivo, FileIO.DeleteDirectoryOption.DeleteAllContents)
        Catch ex As Exception

        End Try

    End Sub

    Public Sub copyFile(ByVal arqOrig As String, ByVal arqDest As String)
        My.Computer.FileSystem.CopyFile(arqOrig, _
arqDest, Microsoft.VisualBasic.FileIO.UIOption.OnlyErrorDialogs, FileIO.UICancelOption.DoNothing)
    End Sub

    'junta os dois arquivos no arquivoDestino
    Public Sub appendFile(ByVal arqOrig As String, ByVal arqDest As String)


        Dim objStreamWriter As StreamWriter
        objStreamWriter = New StreamWriter(arqDest, True)

        'escrevendo o arquivo original
        Dim sLine As String

        'escrevendo o arquivo novo
        Dim objReader As New StreamReader(arqOrig)
        Do
            sLine = objReader.ReadLine()
            If Not sLine Is Nothing Then
                objStreamWriter.WriteLine(sLine)
            End If
        Loop Until sLine Is Nothing
        objStreamWriter.Close()
        objReader.Close()

    End Sub



    Public Sub createFolder(ByVal arquivo As String)
        My.Computer.FileSystem.CreateDirectory(getCurrDir() & "\" & arquivo)
    End Sub

    Public Sub createFolder2(ByVal arquivo As String)
        My.Computer.FileSystem.CreateDirectory(arquivo)
    End Sub

    Public Sub execute(ByVal arquivo As String)
        Dim Scr_hDC As Long
        Scr_hDC = GetDesktopWindow()

        Dim N = getCurrDir() & "\" & arquivo
        Shell(N, AppWinStyle.NormalFocus, False)


        Dim r = ShellExecute(Scr_hDC, "Open", getCurrDir() & "\" & arquivo, _
        "", getCurrDir(), SW_SHOWNORMAL)
        Dim msg
        If r <= 32 Then
            'There was an error
            Select Case r
                Case SE_ERR_FNF
                    msg = "File not found"
                Case SE_ERR_PNF
                    msg = "Path not found"
                Case SE_ERR_ACCESSDENIED
                    msg = "Access denied"
                Case SE_ERR_OOM
                    msg = "Out of memory"
                Case SE_ERR_DLLNOTFOUND
                    msg = "DLL not found"
                Case SE_ERR_SHARE
                    msg = "A sharing violation occurred"
                Case SE_ERR_ASSOCINCOMPLETE
                    msg = "Incomplete or invalid file association"
                Case SE_ERR_DDETIMEOUT
                    msg = "DDE Time out"
                Case SE_ERR_DDEFAIL
                    msg = "DDE transaction failed"
                Case SE_ERR_DDEBUSY
                    msg = "DDE busy"
                Case SE_ERR_NOASSOC
                    msg = "No association for file extension"
                Case ERROR_BAD_FORMAT
                    msg = "Invalid EXE file or error in EXE image"
                Case Else
                    msg = "Unknown error"
            End Select
            MsgBox(msg)
        End If
    End Sub


    Public Sub rename(ByVal arquivo, ByVal novoNome)
        If FolderExists(getCurrDir() & "\" & arquivo) Then
            renameFolder(arquivo, novoNome)
        Else
            renameFile(arquivo, novoNome)
        End If
    End Sub

    Public Sub renameFile(ByVal arquivo, ByVal novoNome)
        My.Computer.FileSystem.RenameFile(getCurrDir() & "\" & arquivo, novoNome)
    End Sub

    Public Sub renameFolder(ByVal arquivo, ByVal novoNome)
        My.Computer.FileSystem.RenameDirectory(getCurrDir() & "\" & arquivo, novoNome)
    End Sub

    Public Function getCurrDir() As String
        Dim objFSO = CreateObject("Scripting.FileSystemObject")
        Dim objFolders = objFSO.GetFolder(".")

        Return objFolders.path
    End Function

    Public Function getCaseSensitivePath(file As String) As String
        Dim objFSO = CreateObject("Scripting.FileSystemObject")

        If FolderExists(file) Then
            Dim objFolders = objFSO.GetFolder(file)
            Return objFolders.path
        Else
            Try

            
                Dim objFolders = objFSO.GetFile(file)
                Return objFolders.path
            Catch ex As Exception

                Throw New ApplicationException("Arquivo '" & file & "' não encontrado!")
            End Try

        End If

    End Function


    Public Function list(ByRef strFolder As String, ByVal flagRecursivo As Boolean) As FileType()
        '   Console.WriteLine("local.list:" & Now)
        If strFolder.EndsWith(".svn") Then
            Return Nothing
        End If
        Dim ret As FileType()

        Dim objFSO = CreateObject("Scripting.FileSystemObject")
        Dim objFolders = objFSO.GetFolder(strFolder)

        Dim objSubFolders = objFolders.SubFolders
        Dim objFiles = objFolders.Files
        Dim file
        Dim folder

        Dim d As Date
        Dim nome As String
        Dim ft As FileType

        '  Console.WriteLine("local.p1:" & Now)
        Dim c = 1
        For Each folder In objSubFolders
            c = c + 1
        Next
        ReDim Preserve ret(c)
        c = 1
        For Each folder In objSubFolders

            d = folder.datelastmodified
            nome = folder.name
            ft = New FileType(folder.path, nome, 0, d, True)
            If (UBound(ret) < c) Then
                c = UBound(ret) + 1
            End If
            ret(c - 1) = ft
            c = c + 1
            If flagRecursivo Then
                Dim retF As FileType() = list(folder.path, True)
                Dim i
                If Not retF Is Nothing Then
                    For Each i In retF
                        If Not i Is Nothing Then
                            ReDim Preserve ret(c)
                            ret(c - 1) = i
                            c = c + 1
                        End If
                    Next
                End If
            End If


            'Console.WriteLine("folder:" & nome)

        Next


        ' Console.WriteLine("local.p2:" & Now)

        Dim ctemp = c

        For Each file In objFiles
            c = c + 1
        Next
        ReDim Preserve ret(c - 2)
        c = ctemp

        'Console.WriteLine("local.p3:" & Now)

        Dim agora As DateTime = Now

        'Dim count As Integer = 0
        For Each file In objFiles
            'ts = Now - agora
            'Console.WriteLine("local.p3a:" & ts.Milliseconds)
            'agora = Now
            d = file.datelastmodified
            nome = file.name
            ft = New FileType(file.path, nome, file.size, d, False)
            ret(c - 1) = ft
            c = c + 1
        Next

        'Console.WriteLine("local.p4:" & Now)
        objSubFolders = Nothing

        objFiles = Nothing
        '  Console.WriteLine("local.listEnd:" & Now)
        Return ret
    End Function
End Class
