Imports System.Drawing
Imports System.Drawing.Drawing2D

Public Class ImageUtils
    Public Shared Function ResizeImage(ByVal image As Image, _
      ByVal size As Size, Optional ByVal preserveAspectRatio As Boolean = True) As Image
      

        Dim newSize As Size = getRatioSize(image, size)

        Dim newImage As Bitmap = New Bitmap(newSize.Width, newSize.Height)
        newImage.SetResolution(300, 300)



        Using graphicsHandle As Graphics = Graphics.FromImage(newImage)
            graphicsHandle.InterpolationMode = InterpolationMode.HighQualityBicubic
            graphicsHandle.SmoothingMode = Drawing2D.SmoothingMode.AntiAlias
            graphicsHandle.TextRenderingHint = Drawing.Text.TextRenderingHint.AntiAlias
            'graphicsHandle.CompositingMode = System.Drawing.Drawing2D.CompositingMode.SourceCopy
            'graphicsHandle.CompositingQuality = System.Drawing.Drawing2D.CompositingQuality.HighQuality

            Dim widthImg As Integer = newSize.Width
            Dim heightImg As Integer = newSize.Height

            graphicsHandle.DrawImage(image, 0, 0, widthImg, heightImg)
        End Using
        Return newImage
    End Function

    Public Shared Function getRatioSize(ByVal image As Image, ByVal sizeDestino As Size) As Size
        Dim newWidth As Integer
        Dim newHeight As Integer
        Dim originalWidth As Integer = image.Width
        Dim originalHeight As Integer = image.Height
        Dim percentWidth As Single = CSng(sizeDestino.Width) / CSng(originalWidth)
        Dim percentHeight As Single = CSng(sizeDestino.Height) / CSng(originalHeight)
        Dim percent As Single
        If percentHeight < percentWidth Then percent = percentHeight Else percent = percentWidth
        newWidth = CInt(originalWidth * percent)
        newHeight = CInt(originalHeight * percent)
        Return New Size(newWidth, newHeight)
    End Function

    Public Shared Function resizeSaveCmd(ByVal util As util, ByVal pathToConvert As String, ByVal image As Image, ByVal arquivo As FileType, ByVal size As Size, ByVal destino As String) As Size
        Dim newSize As Size = getRatioSize(image, size)
        Dim cmd = " """ & arquivo.path & """ -resize " & newSize.Width & "x" & newSize.Height & " """ & destino & """"
        Process.Start(pathToConvert, cmd)
        ' util.executarComando(cmd)
        'Shell(cmd, AppWinStyle.NormalFocus, True)
        Return newSize
    End Function

End Class
