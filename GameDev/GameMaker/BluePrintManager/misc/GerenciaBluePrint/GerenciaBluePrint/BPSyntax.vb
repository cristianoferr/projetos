Public Class BPSyntax
    Dim arrKeywords As ArrayList = New ArrayList()
    Dim arrReservedWords As ArrayList = New ArrayList()

    Public Sub New()
        adicionaKeywords()
    End Sub
    Sub adicionaKeywords()
        arrKeywords.Add("@blueprint")
        arrKeywords.Add("@mod")
        arrKeywords.Add("@factory")
        arrKeywords.Add("@end")
        arrKeywords.Add("+=")
        arrKeywords.Add("=")
        arrKeywords.Add("-=")
        arrKeywords.Add("*=")
        arrKeywords.Add("""")
        arrKeywords.Add("{")
        arrKeywords.Add("}")
        arrKeywords.Add("(")
        arrKeywords.Add(")")

        arrReservedWords.Add("pickOne")
        arrReservedWords.Add("isNull")
        arrReservedWords.Add("isNotNull")
        arrReservedWords.Add("containsTag")
    End Sub

    Sub highLightText(richText As RichTextBox)
        resetaEstilo(richText)
        highLightKeywords(richText)
        highLightLists(richText)
        highLightReserved(richText)

    End Sub

    Private Sub highLightReserved(richText As RichTextBox)
        Dim keyword As String
        Dim newfont As New Font(richText.Font.FontFamily, richText.Font.Size, FontStyle.Italic)
        For Each keyword In arrReservedWords
            formatText(richText, newfont, keyword, Color.Brown)

        Next
    End Sub
    Private Sub resetaEstilo(richText As RichTextBox)
        Dim newfont As New Font(richText.Font.FontFamily, richText.Font.Size, FontStyle.Regular)
        Dim intLength As Integer = richText.TextLength
        richText.Select(0, intLength)
        richText.SelectionFont = newfont
        richText.SelectionColor = Color.Black

    End Sub

    Private Sub highLightKeywords(richText As RichTextBox)
        Dim keyword As String
        Dim newfont As New Font(richText.Font.FontFamily, richText.Font.Size, FontStyle.Bold)
        For Each keyword In arrKeywords
            formatText(richText, newfont, keyword, Color.DarkBlue)

        Next
    End Sub

    Private Function isKeyword(word As String) As Boolean
        Dim keyword As String
        For Each keyword In arrKeywords
            If keyword = word Then Return True
        Next
        Return False
    End Function

    Private Sub highLightLists(richText As RichTextBox)
        Dim newfont As New Font(richText.Font.FontFamily, richText.Font.Size, FontStyle.Italic)
        'formatText(richText, newfont, keyword)

        Dim pos As Integer = 0
        Dim intStartPos As Integer = 1
        Dim intLength As Integer = richText.TextLength
        Do
            pos = intStartPos
            intStartPos = richText.Find("@", intStartPos + 1, intLength, Nothing)
            If (intStartPos > -1) Then
                Dim intSpace = richText.Find(" ", intStartPos, intLength, Nothing)
                If (intSpace > -1) Then
                    richText.Select(intStartPos, intSpace - intStartPos)
                    Dim word As String = richText.SelectedText
                    If (Not isKeyword(word)) Then
                        richText.SelectionFont = newfont
                        richText.SelectionColor = Color.DarkCyan
                        highLightParamAt(intSpace, richText)
                    End If
                End If
            End If


        Loop Until intStartPos = pos Or intStartPos = -1


    End Sub

    Sub highLightParamAt(intSpace As Integer, richText As RichTextBox)
        intSpace = intSpace + 1
        Dim intLength As Integer = richText.TextLength


        Dim intEndPlus As Integer = richText.Find("+", intSpace, intLength, Nothing)
        Dim intEndMinus As Integer = richText.Find("-", intSpace, intLength, Nothing)
        Dim intEndEquals As Integer = richText.Find("=", intSpace, intLength, Nothing)
        Dim intEndVersus As Integer = richText.Find("*", intSpace, intLength, Nothing)
        Dim intEndDiv As Integer = richText.Find("/", intSpace, intLength, Nothing)

        Dim intEndPos As Integer = intLength
        If intEndPlus > intSpace And intEndPlus < intEndPos Then intEndPos = intEndPlus
        If intEndMinus > intSpace And intEndMinus < intEndPos Then intEndPos = intEndMinus
        If intEndEquals > intSpace And intEndEquals < intEndPos Then intEndPos = intEndEquals
        If intEndVersus > intSpace And intEndVersus < intEndPos Then intEndPos = intEndVersus
        If intEndDiv > intSpace And intEndDiv < intEndPos Then intEndPos = intEndDiv
        
        richText.Select(intSpace, intEndPos - intSpace)
        Dim word As String = richText.SelectedText
        richText.SelectionColor = Color.Red

    End Sub


    Private Sub formatText(richText As RichTextBox, newfont As Font, keyword As String, cor As Color)
        'Dim intStartPos As Integer = richText.Find(keyword)

        ' if the string was found, then select it and assign a new font
        'If intStartPos >= 0 Then
        'richText.Select(intStartPos, keyword.Length)
        'richText.SelectionFont = newfont
        'richText.SelectionColor = cor
        'End If


        Dim pos As Integer = 0
        Dim intLength As Integer = richText.TextLength
        Dim intStartPos As Integer = -1
        Do
            pos = intStartPos
            intStartPos = richText.Find(keyword, intStartPos + 1, intLength, Nothing)
            If (intStartPos > -1) Then
                richText.Select(intStartPos, keyword.Length)
                Dim word As String = richText.SelectedText
                richText.SelectionFont = newfont
                richText.SelectionColor = cor
            End If

        Loop Until intStartPos = pos Or intStartPos = -1
    End Sub

End Class
