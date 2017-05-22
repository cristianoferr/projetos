object frmabout: Tfrmabout
  Left = 152
  Top = 130
  Width = 342
  Height = 357
  Caption = 'About'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  OnClose = FormClose
  OnCreate = FormCreate
  PixelsPerInch = 96
  TextHeight = 13
  object Label1: TLabel
    Left = 45
    Top = 2
    Width = 238
    Height = 13
    Caption = 'IRCPre2 Copyright (C) 1996 by Dann M. Daggett II'
  end
  object Label2: TLabel
    Left = 49
    Top = 49
    Width = 231
    Height = 13
    Caption = 'version in Delphi by Cristiano M. Ferreira'
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = [fsBold]
    ParentFont = False
  end
  object Label3: TLabel
    Left = 56
    Top = 65
    Width = 216
    Height = 13
    Caption = 'Visit my page at http://lbworld.cjb.net'
    Font.Charset = DEFAULT_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'MS Sans Serif'
    Font.Style = [fsBold]
    ParentFont = False
  end
  object Label4: TLabel
    Left = 9
    Top = 18
    Width = 311
    Height = 13
    Caption = 'IRCPre2 was written by Dasmius. He can be found on EFNet IRC,'
  end
  object Label5: TLabel
    Left = 98
    Top = 34
    Width = 132
    Height = 13
    Caption = 'in the channel #visualbasic.'
  end
  object Button1: TButton
    Left = 0
    Top = 303
    Width = 329
    Height = 25
    Cancel = True
    Caption = '&Close'
    TabOrder = 0
    OnClick = Button1Click
  end
  object Panel1: TPanel
    Left = 0
    Top = 85
    Width = 329
    Height = 217
    TabOrder = 1
    object tab: TImage
      Left = 1
      Top = 1
      Width = 327
      Height = 215
      Align = alClient
      OnMouseMove = tabMouseMove
    end
  end
  object Timer1: TTimer
    Interval = 1
    OnTimer = Timer1Timer
    Left = 64
    Top = 64
  end
end
