object frmverbot: Tfrmverbot
  Left = 43
  Top = 120
  Width = 679
  Height = 563
  Caption = 'frmverbot'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'MS Sans Serif'
  Font.Style = []
  OldCreateOrder = False
  WindowState = wsMaximized
  OnCreate = FormCreate
  PixelsPerInch = 96
  TextHeight = 13
  object Label6: TLabel
    Left = 8
    Top = 409
    Width = 35
    Height = 13
    Caption = 'Sujeito:'
  end
  object Label7: TLabel
    Left = 8
    Top = 424
    Width = 38
    Height = 13
    Caption = 'Genero:'
  end
  object Label8: TLabel
    Left = 8
    Top = 438
    Width = 26
    Height = 13
    Caption = 'Grau:'
  end
  object Label9: TLabel
    Left = 8
    Top = 355
    Width = 36
    Height = 13
    Caption = 'Tempo:'
  end
  object Label10: TLabel
    Left = 2
    Top = 32
    Width = 54
    Height = 13
    Caption = 'Sentenças:'
  end
  object Label11: TLabel
    Left = 59
    Top = 32
    Width = 38
    Height = 13
    Caption = 'Label11'
  end
  object Label12: TLabel
    Left = 12
    Top = 50
    Width = 28
    Height = 13
    Caption = 'Verbo'
  end
  object Label13: TLabel
    Left = 93
    Top = 51
    Width = 38
    Height = 13
    Caption = 'Adjetivo'
  end
  object Label14: TLabel
    Left = 181
    Top = 53
    Width = 60
    Height = 13
    Caption = 'SubsTantivo'
  end
  object Label15: TLabel
    Left = 274
    Top = 53
    Width = 42
    Height = 13
    Caption = 'Pronome'
  end
  object Label16: TLabel
    Left = 10
    Top = 369
    Width = 27
    Height = 13
    Caption = 'Artigo'
  end
  object esujeito: TLabel
    Left = 48
    Top = 408
    Width = 35
    Height = 13
    Caption = 'Sujeito:'
  end
  object egenero: TLabel
    Left = 48
    Top = 423
    Width = 38
    Height = 13
    Caption = 'Genero:'
  end
  object egrau: TLabel
    Left = 48
    Top = 437
    Width = 26
    Height = 13
    Caption = 'Grau:'
  end
  object etempo: TLabel
    Left = 48
    Top = 354
    Width = 36
    Height = 13
    Caption = 'Tempo:'
  end
  object eartigo: TLabel
    Left = 50
    Top = 368
    Width = 23
    Height = 13
    Caption = 'Grau'
  end
  object Label17: TLabel
    Left = 10
    Top = 384
    Width = 35
    Height = 13
    Caption = 'Pessoa'
  end
  object epessoa: TLabel
    Left = 50
    Top = 383
    Width = 23
    Height = 13
    Caption = 'Grau'
  end
  object etipo: TLabel
    Left = 49
    Top = 396
    Width = 23
    Height = 13
    Caption = 'Grau'
  end
  object Label19: TLabel
    Left = 9
    Top = 397
    Width = 21
    Height = 13
    Caption = 'Tipo'
  end
  object Label1: TLabel
    Left = 4
    Top = 478
    Width = 32
    Height = 13
    Caption = 'Label1'
  end
  object Label2: TLabel
    Left = 4
    Top = 496
    Width = 32
    Height = 13
    Caption = 'Label1'
  end
  object Label3: TLabel
    Left = 8
    Top = 450
    Width = 43
    Height = 13
    Caption = 'Negativo'
  end
  object enegat: TLabel
    Left = 48
    Top = 449
    Width = 26
    Height = 13
    Caption = 'Grau:'
  end
  object Label4: TLabel
    Left = 600
    Top = 256
    Width = 32
    Height = 13
    Caption = 'Label4'
  end
  object Label5: TLabel
    Left = 600
    Top = 240
    Width = 32
    Height = 13
    Caption = 'Label5'
  end
  object Button1: TButton
    Left = 360
    Top = 6
    Width = 75
    Height = 25
    Caption = 'Calcula'
    Default = True
    TabOrder = 1
    OnClick = Button1Click
  end
  object DBGrid1: TDBGrid
    Left = 0
    Top = 66
    Width = 91
    Height = 85
    TabStop = False
    DataSource = dsverbo
    TabOrder = 2
    TitleFont.Charset = DEFAULT_CHARSET
    TitleFont.Color = clWindowText
    TitleFont.Height = -11
    TitleFont.Name = 'MS Sans Serif'
    TitleFont.Style = []
  end
  object Edit1: TEdit
    Left = 0
    Top = 5
    Width = 354
    Height = 21
    TabOrder = 0
    Text = 'Como está vc?'
  end
  object Memo1: TMemo
    Left = 584
    Top = 1
    Width = 59
    Height = 151
    TabOrder = 3
  end
  object DBGrid2: TDBGrid
    Left = 88
    Top = 66
    Width = 90
    Height = 102
    TabStop = False
    DataSource = dsadj
    TabOrder = 4
    TitleFont.Charset = DEFAULT_CHARSET
    TitleFont.Color = clWindowText
    TitleFont.Height = -11
    TitleFont.Name = 'MS Sans Serif'
    TitleFont.Style = []
  end
  object DBGrid3: TDBGrid
    Left = 176
    Top = 66
    Width = 91
    Height = 85
    TabStop = False
    DataSource = dssubst
    TabOrder = 5
    TitleFont.Charset = DEFAULT_CHARSET
    TitleFont.Color = clWindowText
    TitleFont.Height = -11
    TitleFont.Name = 'MS Sans Serif'
    TitleFont.Style = []
  end
  object DBGrid4: TDBGrid
    Left = 269
    Top = 66
    Width = 180
    Height = 100
    TabStop = False
    DataSource = dspronome
    TabOrder = 6
    TitleFont.Charset = DEFAULT_CHARSET
    TitleFont.Color = clWindowText
    TitleFont.Height = -11
    TitleFont.Name = 'MS Sans Serif'
    TitleFont.Style = []
  end
  object Memo2: TMemo
    Left = 476
    Top = 1
    Width = 108
    Height = 151
    TabOrder = 7
  end
  object DBGrid5: TDBGrid
    Left = 0
    Top = 164
    Width = 451
    Height = 77
    TabStop = False
    DataSource = dssentenca
    TabOrder = 8
    TitleFont.Charset = DEFAULT_CHARSET
    TitleFont.Color = clWindowText
    TitleFont.Height = -11
    TitleFont.Name = 'MS Sans Serif'
    TitleFont.Style = []
  end
  object DBGrid6: TDBGrid
    Left = 1
    Top = 247
    Width = 273
    Height = 79
    TabStop = False
    DataSource = dsitem
    TabOrder = 9
    TitleFont.Charset = DEFAULT_CHARSET
    TitleFont.Color = clWindowText
    TitleFont.Height = -11
    TitleFont.Name = 'MS Sans Serif'
    TitleFont.Style = []
  end
  object DBGrid7: TDBGrid
    Left = 403
    Top = 398
    Width = 314
    Height = 96
    TabStop = False
    DataSource = dsconjug
    TabOrder = 10
    TitleFont.Charset = DEFAULT_CHARSET
    TitleFont.Color = clWindowText
    TitleFont.Height = -11
    TitleFont.Name = 'MS Sans Serif'
    TitleFont.Style = []
  end
  object DBGrid8: TDBGrid
    Left = 275
    Top = 246
    Width = 325
    Height = 77
    TabStop = False
    DataSource = dsaverbo
    TabOrder = 11
    TitleFont.Charset = DEFAULT_CHARSET
    TitleFont.Color = clWindowText
    TitleFont.Height = -11
    TitleFont.Name = 'MS Sans Serif'
    TitleFont.Style = []
  end
  object DBGrid9: TDBGrid
    Left = 395
    Top = 326
    Width = 359
    Height = 70
    TabStop = False
    DataSource = dsabrev
    TabOrder = 12
    TitleFont.Charset = DEFAULT_CHARSET
    TitleFont.Color = clWindowText
    TitleFont.Height = -11
    TitleFont.Name = 'MS Sans Serif'
    TitleFont.Style = []
  end
  object DBGrid10: TDBGrid
    Left = 96
    Top = 326
    Width = 296
    Height = 78
    TabStop = False
    DataSource = dsoracao
    TabOrder = 13
    TitleFont.Charset = DEFAULT_CHARSET
    TitleFont.Color = clWindowText
    TitleFont.Height = -11
    TitleFont.Name = 'MS Sans Serif'
    TitleFont.Style = []
  end
  object DBGrid11: TDBGrid
    Left = 95
    Top = 403
    Width = 300
    Height = 97
    TabStop = False
    DataSource = dspalora
    TabOrder = 14
    TitleFont.Charset = DEFAULT_CHARSET
    TitleFont.Color = clWindowText
    TitleFont.Height = -11
    TitleFont.Name = 'MS Sans Serif'
    TitleFont.Style = []
  end
  object log: TMemo
    Left = 453
    Top = 157
    Width = 184
    Height = 83
    TabOrder = 15
  end
  object Database1: TDatabase
    AliasName = 'verbot'
    Connected = True
    DatabaseName = 'db'
    LoginPrompt = False
    SessionName = 'Default'
    Left = 448
    Top = 32
  end
  object tbverbo: TTable
    Active = True
    DatabaseName = 'db'
    IndexFieldNames = 'item'
    TableName = 'verbos'
    Left = 48
    Top = 32
    object tbverboitem: TStringField
      FieldName = 'item'
      Required = True
      Size = 50
    end
  end
  object dsverbo: TDataSource
    DataSet = tbverbo
    Left = 72
    Top = 32
  end
  object tbadj: TTable
    Active = True
    DatabaseName = 'db'
    IndexFieldNames = 'item'
    TableName = 'adjetivo'
    Left = 48
    Top = 59
    object tbadjitem: TStringField
      DisplayWidth = 48
      FieldName = 'item'
      Size = 40
    end
    object tbadjtipo: TIntegerField
      DisplayWidth = 5
      FieldName = 'tipo'
    end
    object tbadjtpsujeito: TIntegerField
      DisplayWidth = 9
      FieldName = 'tpsujeito'
    end
    object tbadjgenero: TIntegerField
      DisplayWidth = 8
      FieldName = 'genero'
    end
    object tbadjadjbom: TBooleanField
      DisplayWidth = 7
      FieldName = 'adjbom'
    end
    object tbadjgrau: TIntegerField
      DisplayWidth = 12
      FieldName = 'grau'
    end
    object tbadjnegativo: TBooleanField
      FieldName = 'negativo'
    end
  end
  object dsadj: TDataSource
    DataSet = tbadj
    Left = 72
    Top = 53
  end
  object tbsubst: TTable
    Active = True
    DatabaseName = 'db'
    IndexFieldNames = 'item'
    TableName = 'subst'
    Left = 104
    Top = 34
    object tbsubstitem: TStringField
      FieldName = 'item'
      Size = 50
    end
    object tbsubsttipo: TIntegerField
      FieldName = 'tipo'
    end
  end
  object dssubst: TDataSource
    DataSet = tbsubst
    Left = 128
    Top = 34
  end
  object tbpronome: TTable
    Active = True
    DatabaseName = 'db'
    IndexFieldNames = 'item'
    TableName = 'pronome'
    Left = 103
    Top = 56
    object tbpronomeitem: TStringField
      DisplayWidth = 14
      FieldName = 'item'
      Size = 50
    end
    object tbpronomepessoa: TIntegerField
      DisplayWidth = 7
      FieldName = 'pessoa'
    end
    object tbpronomesingular: TBooleanField
      DisplayWidth = 6
      FieldName = 'singular'
    end
    object tbpronometppronome: TIntegerField
      DisplayWidth = 9
      FieldName = 'tppronome'
    end
    object tbpronomegenero: TIntegerField
      DisplayWidth = 8
      FieldName = 'genero'
    end
  end
  object dspronome: TDataSource
    DataSet = tbpronome
    Left = 127
    Top = 56
  end
  object tbsentenca: TTable
    Active = True
    BeforeDelete = tbsentencaBeforeDelete
    DatabaseName = 'db'
    IndexName = 'PrimaryKey'
    TableName = 'sentenca'
    Left = 157
    Top = 35
    object tbsentencacod_estrutura: TIntegerField
      DisplayWidth = 13
      FieldName = 'cod_estrutura'
    end
    object tbsentencatpsuj: TIntegerField
      DisplayWidth = 7
      FieldName = 'tpsuj'
    end
    object tbsentencagensuj: TIntegerField
      DisplayWidth = 10
      FieldName = 'gensuj'
    end
    object tbsentencapessoa: TIntegerField
      DisplayWidth = 7
      FieldName = 'pessoa'
    end
    object tbsentencagrau: TIntegerField
      DisplayWidth = 6
      FieldName = 'grau'
    end
    object tbsentencatempo: TIntegerField
      DisplayWidth = 7
      FieldName = 'tempo'
    end
    object tbsentencaresposta: TIntegerField
      DisplayWidth = 9
      FieldName = 'resposta'
    end
    object tbsentencatipo: TIntegerField
      DisplayWidth = 4
      FieldName = 'tipo'
    end
    object tbsentencanegativo: TBooleanField
      FieldName = 'negativo'
    end
  end
  object dssentenca: TDataSource
    DataSet = tbsentenca
    Left = 181
    Top = 35
  end
  object tbitemsentenca: TTable
    Active = True
    DatabaseName = 'db'
    IndexFieldNames = 'cod_sentenca'
    MasterFields = 'cod_estrutura'
    MasterSource = dssentenca
    TableName = 'itemsentenca'
    Left = 157
    Top = 56
    object tbitemsentencacod_item: TIntegerField
      DisplayWidth = 8
      FieldName = 'cod_item'
    end
    object tbitemsentencacod_sentenca: TIntegerField
      DisplayWidth = 7
      FieldName = 'cod_sentenca'
    end
    object tbitemsentencatipo: TIntegerField
      DisplayWidth = 6
      FieldName = 'tipo'
    end
    object tbitemsentencapal: TStringField
      DisplayWidth = 12
      FieldName = 'pal'
      Size = 50
    end
  end
  object dsitem: TDataSource
    DataSet = tbitemsentenca
    Left = 181
    Top = 56
  end
  object tbaverbo: TTable
    Active = True
    DatabaseName = 'db'
    IndexFieldNames = 'item'
    TableName = 'averbo'
    Left = 207
    Top = 57
    object tbaverboitem: TStringField
      DisplayWidth = 13
      FieldName = 'item'
      Size = 50
    end
    object tbaverbotempo_verb: TIntegerField
      DisplayWidth = 8
      FieldName = 'tempo_verb'
    end
    object tbaverbopessoa_verb: TIntegerField
      DisplayWidth = 8
      FieldName = 'pessoa_verb'
    end
    object tbaverbotpsuj_verb: TIntegerField
      DisplayWidth = 5
      FieldName = 'tpsuj_verb'
    end
    object tbaverboindic_verb: TBooleanField
      DisplayWidth = 9
      FieldName = 'indic_verb'
    end
    object tbaverboverbo: TStringField
      DisplayWidth = 10
      FieldName = 'verbo'
      Size = 50
    end
    object tbaverbonegativo: TBooleanField
      FieldName = 'negativo'
    end
  end
  object dsaverbo: TDataSource
    DataSet = tbaverbo
    Left = 231
    Top = 57
  end
  object tbconjug: TTable
    Active = True
    DatabaseName = 'db'
    IndexName = 'PrimaryKey'
    TableName = 'conjugacao'
    Left = 207
    Top = 35
    object tbconjugfim_conj: TStringField
      DisplayWidth = 8
      FieldName = 'fim_conj'
      Size = 10
    end
    object tbconjugterm_conj: TStringField
      DisplayWidth = 9
      FieldName = 'term_conj'
      Size = 5
    end
    object tbconjugtempo_conj: TIntegerField
      DisplayWidth = 7
      FieldName = 'tempo_conj'
    end
    object tbconjugpessoa_conj: TIntegerField
      DisplayWidth = 9
      FieldName = 'pessoa_conj'
    end
    object tbconjugtpsuj_conj: TIntegerField
      DisplayWidth = 6
      FieldName = 'tpsuj_conj'
    end
    object tbconjugindic_conj: TBooleanField
      DisplayWidth = 5
      FieldName = 'indic_conj'
    end
    object tbconjugcerto_conj: TBooleanField
      DisplayWidth = 5
      FieldName = 'certo_conj'
    end
  end
  object dsconjug: TDataSource
    DataSet = tbconjug
    Left = 231
    Top = 35
  end
  object tbabrev: TTable
    Active = True
    DatabaseName = 'db'
    IndexName = 'PrimaryKey'
    TableName = 'abreviacao'
    Left = 258
    Top = 31
    object tbabrevitem: TStringField
      DisplayWidth = 8
      FieldName = 'item'
      Size = 50
    end
    object tbabrevtroca1: TStringField
      DisplayWidth = 8
      FieldName = 'troca1'
      Size = 50
    end
    object tbabrevtroca2: TStringField
      DisplayWidth = 7
      FieldName = 'troca2'
      Size = 50
    end
    object tbabrevtroca3: TStringField
      DisplayWidth = 7
      FieldName = 'troca3'
      Size = 50
    end
    object tbabrevtempo: TIntegerField
      DisplayWidth = 6
      FieldName = 'tempo'
    end
    object tbabrevpessoa: TIntegerField
      DisplayWidth = 6
      FieldName = 'pessoa'
    end
    object tbabrevtpsuj: TIntegerField
      DisplayWidth = 5
      FieldName = 'tpsuj'
    end
    object tbabrevindic: TBooleanField
      DisplayWidth = 6
      FieldName = 'indic'
    end
    object tbabrevbom: TBooleanField
      DisplayWidth = 6
      FieldName = 'bom'
    end
    object tbabrevnegativo: TBooleanField
      FieldName = 'negativo'
    end
  end
  object dsabrev: TDataSource
    DataSet = tbabrev
    Left = 282
    Top = 31
  end
  object oracao: TTable
    Active = True
    BeforeDelete = oracaoBeforeDelete
    DatabaseName = 'db'
    IndexFieldNames = 'cod_conh'
    TableName = 'oracao'
    Left = 313
    Top = 35
    object oracaocod_conh: TIntegerField
      DisplayWidth = 9
      FieldName = 'cod_conh'
    end
    object oracaonick_conh: TStringField
      DisplayWidth = 10
      FieldName = 'nick_conh'
      Size = 50
    end
    object oracaotempo: TIntegerField
      DisplayWidth = 6
      FieldName = 'tempo'
    end
    object oracaopessoa: TIntegerField
      DisplayWidth = 7
      FieldName = 'pessoa'
    end
    object oracaotpsuj: TIntegerField
      DisplayWidth = 6
      FieldName = 'tpsuj'
    end
    object oracaoindic: TBooleanField
      DisplayWidth = 7
      FieldName = 'indic'
    end
    object oracaogensuj: TIntegerField
      DisplayWidth = 7
      FieldName = 'gensuj'
    end
    object oracaograu: TIntegerField
      DisplayWidth = 4
      FieldName = 'grau'
    end
    object oracaotipo: TIntegerField
      DisplayWidth = 4
      FieldName = 'tipo'
    end
    object oracaonegativo: TBooleanField
      DisplayWidth = 8
      FieldName = 'negativo'
    end
  end
  object dsoracao: TDataSource
    DataSet = oracao
    Left = 337
    Top = 35
  end
  object tbpalora: TTable
    Active = True
    DatabaseName = 'db'
    Filtered = True
    IndexFieldNames = 'cod_conh'
    MasterFields = 'cod_conh'
    MasterSource = dsoracao
    TableName = 'palora'
    Left = 313
    Top = 57
    object tbpaloracod_pal: TIntegerField
      DisplayWidth = 10
      FieldName = 'cod_pal'
    end
    object tbpaloracod_conh: TIntegerField
      DisplayWidth = 12
      FieldName = 'cod_conh'
    end
    object tbpalorapal: TStringField
      DisplayWidth = 10
      FieldName = 'pal'
      Size = 50
    end
    object tbpaloratppal: TIntegerField
      DisplayWidth = 7
      FieldName = 'tppal'
    end
    object tbpalorasujeito: TBooleanField
      DisplayWidth = 9
      FieldName = 'sujeito'
    end
  end
  object dspalora: TDataSource
    DataSet = tbpalora
    Left = 337
    Top = 57
  end
end
