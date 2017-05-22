unit uverbot;

interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  Db, DBTables, StdCtrls, Grids, DBGrids,uentrada,uprocessa,usaida;


type
  Tfrmverbot = class(TForm)
    Database1: TDatabase;
    tbverbo: TTable;
    dsverbo: TDataSource;
    Button1: TButton;
    DBGrid1: TDBGrid;
    Edit1: TEdit;
    Memo1: TMemo;
    tbverboitem: TStringField;
    tbadj: TTable;
    dsadj: TDataSource;
    DBGrid2: TDBGrid;
    tbsubst: TTable;
    dssubst: TDataSource;
    DBGrid3: TDBGrid;
    tbsubstitem: TStringField;
    Label6: TLabel;
    Label7: TLabel;
    Label8: TLabel;
    Label9: TLabel;
    Label10: TLabel;
    Label11: TLabel;
    tbpronome: TTable;
    dspronome: TDataSource;
    tbpronomeitem: TStringField;
    tbpronomepessoa: TIntegerField;
    tbpronomesingular: TBooleanField;
    tbpronometppronome: TIntegerField;
    tbpronomegenero: TIntegerField;
    Label12: TLabel;
    Label13: TLabel;
    Label14: TLabel;
    DBGrid4: TDBGrid;
    Label15: TLabel;
    Label16: TLabel;
    esujeito: TLabel;
    egenero: TLabel;
    egrau: TLabel;
    etempo: TLabel;
    eartigo: TLabel;
    Label17: TLabel;
    epessoa: TLabel;
    etipo: TLabel;
    Label19: TLabel;
    Memo2: TMemo;
    tbadjitem: TStringField;
    tbadjtipo: TIntegerField;
    tbadjtpsujeito: TIntegerField;
    tbadjgenero: TIntegerField;
    tbadjadjbom: TBooleanField;
    tbsentenca: TTable;
    dssentenca: TDataSource;
    tbsentencatpsuj: TIntegerField;
    tbsentencagensuj: TIntegerField;
    tbsentencapessoa: TIntegerField;
    tbsentencagrau: TIntegerField;
    tbsentencatempo: TIntegerField;
    tbsentencaresposta: TIntegerField;
    tbsentencatipo: TIntegerField;
    tbitemsentenca: TTable;
    dsitem: TDataSource;
    tbitemsentencacod_item: TIntegerField;
    tbitemsentencacod_sentenca: TIntegerField;
    tbitemsentencatipo: TIntegerField;
    tbitemsentencapal: TStringField;
    DBGrid5: TDBGrid;
    tbsentencacod_estrutura: TIntegerField;
    DBGrid6: TDBGrid;
    tbadjgrau: TIntegerField;
    DBGrid7: TDBGrid;
    tbaverbo: TTable;
    dsaverbo: TDataSource;
    DBGrid8: TDBGrid;
    tbaverboitem: TStringField;
    tbaverbotempo_verb: TIntegerField;
    tbaverbopessoa_verb: TIntegerField;
    tbaverbotpsuj_verb: TIntegerField;
    tbaverboindic_verb: TBooleanField;
    tbaverboverbo: TStringField;
    Label1: TLabel;
    Label2: TLabel;
    tbconjug: TTable;
    tbconjugfim_conj: TStringField;
    tbconjugterm_conj: TStringField;
    tbconjugtempo_conj: TIntegerField;
    tbconjugpessoa_conj: TIntegerField;
    tbconjugtpsuj_conj: TIntegerField;
    tbconjugindic_conj: TBooleanField;
    dsconjug: TDataSource;
    tbabrev: TTable;
    dsabrev: TDataSource;
    tbabrevitem: TStringField;
    tbabrevtroca1: TStringField;
    tbabrevtroca2: TStringField;
    tbabrevtroca3: TStringField;
    tbabrevtempo: TIntegerField;
    tbabrevpessoa: TIntegerField;
    tbabrevtpsuj: TIntegerField;
    tbabrevindic: TBooleanField;
    tbabrevbom: TBooleanField;
    DBGrid9: TDBGrid;
    tbconjugcerto_conj: TBooleanField;
    oracao: TTable;
    dsoracao: TDataSource;
    tbpalora: TTable;
    dspalora: TDataSource;
    oracaocod_conh: TIntegerField;
    oracaonick_conh: TStringField;
    oracaotempo: TIntegerField;
    oracaopessoa: TIntegerField;
    oracaotpsuj: TIntegerField;
    oracaoindic: TBooleanField;
    tbpaloracod_pal: TIntegerField;
    tbpaloracod_conh: TIntegerField;
    tbpalorapal: TStringField;
    tbpaloratppal: TIntegerField;
    tbpalorasujeito: TBooleanField;
    DBGrid10: TDBGrid;
    DBGrid11: TDBGrid;
    oracaogensuj: TIntegerField;
    oracaograu: TIntegerField;
    oracaotipo: TIntegerField;
    tbadjnegativo: TBooleanField;
    tbsubsttipo: TIntegerField;
    tbsentencanegativo: TBooleanField;
    tbaverbonegativo: TBooleanField;
    tbabrevnegativo: TBooleanField;
    oracaonegativo: TBooleanField;
    Label3: TLabel;
    enegat: TLabel;
    Label4: TLabel;
    Label5: TLabel;
    log: TMemo;
    procedure newmsg(de,msg:string);
    procedure FormCreate(Sender: TObject);
    procedure init;
    procedure Button1Click(Sender: TObject);
    procedure tbsentencaBeforeDelete(DataSet: TDataSet);
    procedure oracaoBeforeDelete(DataSet: TDataSet);
    procedure say(s:string);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  frmverbot: Tfrmverbot;
  sent:tiposent;
  vsuj,nsent:integer;
  from:String;

implementation
uses uirc;
{$R *.DFM}

procedure tfrmverbot.say(s:string);
begin
  log.lines.add('<Bot> '+s);
end;
procedure tfrmverbot.init;
var
  c:integer;
begin
  for c:=1 to maxsent do begin
    sent[c].npal:=0;
    sent[c].verbo:='';
    sent[c].verboaux:='';
    sent[c].subs:='';
    sent[c].tipo:=0;
    sent[c].tpsuj:=0;
    sent[c].negat:=false;
    sent[c].adj:='';
  end;
end;
procedure tfrmverbot.newmsg(de,msg:string);
begin
  frmirc.sendcmd('/msg '+de+' '+msg);
  from:=de;
end;

procedure Tfrmverbot.FormCreate(Sender: TObject);
begin
nsent:=1;
tbverbo.open;
tbabrev.open;
tbadj.open;
tbpronome.open;
tbsubst.open;
tbsentenca.open;
tbitemsentenca.open;
tbconjug.open;
tbaverbo.open;
oracao.open;
tbpalora.open;
init;

end;
procedure Tfrmverbot.Button1Click(Sender: TObject);
begin
entrada(edit1.text);
from:='cristiano';
log.lines.add('<'+from+'>'+edit1.text);
inicioprocesso;
processa(from,sent,nsent);
gravasentenca;
listapal;
label1.caption:=pegasujeito(sent[nsent]);
label2.caption:=pegapredicado(sent[nsent]);
label4.caption:=pegaresposta(sent[nsent],pegapergunta(sent[nsent]));
if procbooresposta(sent[nsent]) then begin
label5.caption:='TRUE';
say('Sim');
end
else begin
label5.caption:='FALSE';
if label4.caption='' then say('Nao') else
say(label4.caption);
end;
end;

procedure Tfrmverbot.tbsentencaBeforeDelete(DataSet: TDataSet);
begin
while tbitemsentenca.recordcount>0 do
  tbitemsentenca.delete;
end;

procedure Tfrmverbot.oracaoBeforeDelete(DataSet: TDataSet);
begin
while tbpalora.recordcount>0 do
  tbpalora.delete;
end;

end.
