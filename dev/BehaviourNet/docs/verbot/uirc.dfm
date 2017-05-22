object frmirc: Tfrmirc
  Left = 170
  Top = 139
  Width = 592
  Height = 376
  Caption = 
    'IRC in Delphi by Cristiano M. Ferreira - visit my page at http:/' +
    '/lbworld.cjb.net'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  Menu = MainMenu1
  OldCreateOrder = False
  OnCreate = FormCreate
  PixelsPerInch = 96
  TextHeight = 13
  object incoming: TMemo
    Left = 0
    Top = 20
    Width = 441
    Height = 286
    Lines.Strings = (
      'incoming')
    ReadOnly = True
    ScrollBars = ssVertical
    TabOrder = 0
    OnChange = incomingChange
  end
  object namelist: TListBox
    Left = 444
    Top = 0
    Width = 140
    Height = 330
    Align = alRight
    ItemHeight = 13
    Items.Strings = (
      'Cristiano M. Ferreira'
      'lebeau@linuxbr.com.br'
      'http://lbworld.cjb.net')
    TabOrder = 1
  end
  object outgoing: TEdit
    Left = 0
    Top = 307
    Width = 441
    Height = 21
    TabOrder = 2
    Text = '/join #tuba'
    OnKeyPress = outgoingKeyPress
  end
  object topic: TEdit
    Left = 0
    Top = 0
    Width = 441
    Height = 21
    TabOrder = 3
  end
  object Memo1: TMemo
    Left = 240
    Top = 32
    Width = 185
    Height = 89
    Lines.Strings = (
      'Memo1')
    TabOrder = 4
  end
  object MainMenu1: TMainMenu
    Left = 88
    Top = 104
    object Arquivo1: TMenuItem
      Caption = '&File'
      object Connectar1: TMenuItem
        Caption = '&Conectar'
        OnClick = Connectar1Click
      end
    end
    object About1: TMenuItem
      Caption = '&About'
      OnClick = About1Click
    end
    object Verbot1: TMenuItem
      Caption = 'Verbot'
      OnClick = Verbot1Click
    end
  end
  object tcp1: TClientSocket
    Active = True
    Address = '127.0.0.1'
    ClientType = ctNonBlocking
    Port = 6667
    OnConnect = tcp1Connect
    OnRead = tcp1Read
    Left = 144
    Top = 16
  end
  object Powersock1: TPowersock
    Port = 0
    ReportLevel = 0
    Left = 216
    Top = 88
  end
end
