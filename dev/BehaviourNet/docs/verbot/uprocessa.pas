unit uprocessa;
interface
uses   Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  Db, DBTables, StdCtrls, Grids, DBGrids,uentrada;
const
  maxpessoas=100;
type
 tpessoa=record
      sent:tiposent;
      nsent:integer;
      from:string;
  end;


procedure processa(from:string;var sent:tiposent;max:integer);
procedure inicioprocesso;
procedure listapal;
procedure gravasentenca;
procedure procsent;
function pegasujeito(sent:tsent):string;
function pegapredicado(sent:tsent):string;
function issuj(p:integer;sent:tsent):boolean;
procedure addoracao(sent:tsent;from:string);
function procbooresposta(sent:tsent):boolean;
function pegapergunta(sent:tsent):string;
function pegaresposta(sent:tsent;keyw:String):string;

var
  pessoas:array[1..maxpessoas] of tpessoa;
  npes,pospes:integer;
implementation
uses uverbot;
function procbooresposta(sent:tsent):boolean;
var
  s,pal:string;
  suj,found:boolean;
  c:integer;
begin
  frmverbot.oracao.first;
  s:='';
  suj:=false;
  found:=false;
  if sent.tipo=tpinterrog then
  with sent do begin
    for c:=1 to npal do begin
      if (pals[c].tppal=paladj) or (pals[c].tppal=palsubst) then
        pal:=pals[c].pal;
    end;

  while (not frmverbot.oracao.eof) and (s='') do begin
    if (pessoa=frmverbot.oracaopessoa.value) and
      (gensuj=frmverbot.oracaogensuj.value) and
      (tempo=frmverbot.oracaotempo.value) and
      (frmverbot.oracaotipo.value<>tpinterrog) and
      (tpsuj=frmverbot.oracaotpsuj.value) then begin
         frmverbot.tbpalora.first;
         while not frmverbot.tbpalora.eof do begin
           if (not frmverbot.tbpalorasujeito.value) and ((frmverbot.tbpaloratppal.value=paladj) or (frmverbot.tbpaloratppal.value=palsubst)) then
           if frmverbot.tbpalorapal.value=pal then begin
               found:=true;
           end;
           frmverbot.tbpalora.next;
        end;
      end;
    frmverbot.oracao.next;
    end;
end;
  procbooresposta:=found;

end;




function pegapergunta(sent:tsent):string;
var
  s:string;
  c:integer;
begin
  with sent do begin
  for c:=1 to npal do         
  begin
    if (pals[c].tppal=paladj) or (pals[c].tppal=palsubst) then
      pegapergunta:=trim(pals[c].pal);
  end;
  end;
end;

function pegaresposta(sent:tsent;keyw:String):string;
var
  s:string;
  suj,found:boolean;
begin
  frmverbot.oracao.first;
  s:='';
  suj:=false;
  found:=false;
  with sent do
  while (not frmverbot.oracao.eof) and (s='') do begin
    if (pessoa=frmverbot.oracaopessoa.value) and
      (gensuj=frmverbot.oracaogensuj.value) and
      (tempo=frmverbot.oracaotempo.value) and
      (frmverbot.oracaotipo.value<>2) and
      (tpsuj=frmverbot.oracaotpsuj.value) then begin
         frmverbot.tbpalora.first;
         while not frmverbot.tbpalora.eof do begin
           if trim(frmverbot.tbpalorapal.value)=keyw then begin
               found:=true;
               suj:=not frmverbot.tbpalorasujeito.value;
           end;
           frmverbot.tbpalora.next;
        end;
         frmverbot.tbpalora.first;
         if found then begin
         if suj then frmverbot.tbpalora.filter:='sujeito=true'
         else
         frmverbot.tbpalora.filter:='sujeito=false';

         while not frmverbot.tbpalora.eof do begin
           if (frmverbot.tbpaloratppal.value=palsubst)
           or (frmverbot.tbpaloratppal.value=paladj)
           then
           s:=s+frmverbot.tbpalorapal.value+' ';
           frmverbot.tbpalora.next;
         end;
         frmverbot.tbpalora.filter:='';
         end;
      end;
    frmverbot.oracao.next;
  end;
  pegaresposta:=s;
end;
procedure addoracao(sent:tsent;from:string);
var
  d,e:longint;
  count:longint;
  exist,incomp:boolean;
begin
  frmverbot.oracao.first;
  exist:=false;
  frmverbot.oracao.close;
  frmverbot.oracao.open;
  with sent do begin
  while not frmverbot.oracao.eof do begin
//    frmverbot.canvas.textout(1,count,'');
    if (frmverbot.oracaotpsuj.value=tpsuj) and (frmverbot.oracaogensuj.value=gensuj)
    and (frmverbot.oracaopessoa.value=pessoa) and (frmverbot.oracaograu.value=grau)
    and (frmverbot.oracaonegativo.value=negat)
    and (frmverbot.oracaotempo.value=tempo) and (frmverbot.oracaonick_conh.value=from)
    and (frmverbot.oracaotipo.value=tipo) then begin
       frmverbot.tbpalora.first;
       e:=1;
       for d:=1 to frmverbot.tbpalora.recordcount do begin
         if (frmverbot.tbpaloratppal.value=pals[d].tppal) and (frmverbot.tbpalorapal.value=pals[d].pal)
         and (frmverbot.tbpalorasujeito.value=issuj(d,sent))
         then inc(e);
         frmverbot.tbpalora.next;
       end;
       if d=e then
       exist:=true;
    end;
    frmverbot.oracao.next;
  end;
  incomp:=false;
  if exist=false then begin
    frmverbot.oracao.last;
    count:=frmverbot.oracaocod_conh.value+1;
    frmverbot.oracao.insert;
    frmverbot.oracaocod_conh.value:=count;
    frmverbot.oracaonick_conh.value:=from;
    frmverbot.oracaotpsuj.value:=tpsuj;
    frmverbot.oracaonegativo.value:=negat;
    frmverbot.oracaogensuj.value:=gensuj;
    frmverbot.oracaograu.value:=grau;
    frmverbot.oracaopessoa.value:=pessoa;
    frmverbot.oracaotempo.value:=tempo;
    frmverbot.oracaotipo.value:=tipo;
    frmverbot.oracao.post;
    for d:=1 to npal do begin
    frmverbot.tbpalora.insert;
    frmverbot.tbpaloracod_pal.value:=d;
    frmverbot.tbpaloracod_conh.value:=count;
    frmverbot.tbpaloratppal.value:=pals[d].tppal;
    frmverbot.tbpalorapal.value:=pals[d].pal;
    frmverbot.tbpalorasujeito.value:=issuj(d,sent);
    frmverbot.tbpalora.post;
    end;

  end;
end;
                 
end;
function pegapredicado(sent:tsent):string;
var
  c:integer;
  found:boolean;
  s:string;
begin
  with sent do begin
    c:=1;
    found:=false;
    s:='';
    while (pals[c].tppal<>palverbo) and (pals[c].tppal<>palverboaux)and (c<npal) do begin
      inc(c);
    end;
    while (c<=npal) do begin
     s:=s+pals[c].pal+' ';
     inc(c);
    end;
    delete(s,length(s),1);
  end;
  pegapredicado:=s;
end;
function issuj(p:integer;sent:tsent):boolean;
var
  c:integer;
begin
  with sent do begin
    c:=1;
    while (pals[c].tppal<>palverbo) and (pals[c].tppal<>palverboaux)and (c<npal) do begin
      inc(c);
    end;
  end;
  issuj:=(c>p);
end;

function pegasujeito(sent:tsent):string;
var
  c:integer;
  found:boolean;
  s:string;
begin
  with sent do begin
    c:=1;
    found:=false;
    s:='';
    while (pals[c].tppal<>palverbo) and (pals[c].tppal<>palverboaux)and (c<npal) do begin
      s:=s+pals[c].pal+' ';
      inc(c);
    end;
    vsuj:=c;
    delete(s,length(s),1);
  end;
  pegasujeito:=s;
end;

procedure inicioprocesso;
begin
  npes:=0;
  pospes:=0;
end;
procedure processa(from:string;var sent:tiposent;max:integer);
var
  c:integer;
begin
  for c:=1 to npes do begin
    if pessoas[c].from=from then pospes:=c;
  end;
  if pospes>0 then begin
  if pessoas[pospes].from=from then begin
    pessoas[pospes].from:=from;
    pessoas[pospes].sent:=sent;
    pessoas[pospes].nsent:=max;

  end;
  end else
  begin
    inc(pospes);
    pessoas[pospes].from:=from;
    pessoas[pospes].sent:=sent;
    pessoas[pospes].nsent:=max;
  end;
  addoracao(pessoas[pospes].sent[1],from);
end;
procedure listapal;
var
  c:integer;
begin
  frmverbot.memo2.lines.clear;
  with pessoas[pospes].sent[pessoas[pospes].nsent] do
  for c:=1 to npal do
    frmverbot.memo2.lines.add(inttostr(pals[c].tppal)+':'+gettipopal(pals[c].tppal));
end;
procedure gravasentenca;
var
  d,e,zero:longint;
  count:longint;
  exist,incomp:boolean;
begin
  frmverbot.tbsentenca.first;
  exist:=false;
  frmverbot.tbsentenca.close;
  frmverbot.tbsentenca.open;
  with pessoas[pospes].sent[pessoas[pospes].nsent] do begin
  while not frmverbot.tbsentenca.eof do begin
    frmverbot.canvas.textout(1,count,'');
    if (frmverbot.tbsentencatpsuj.value=tpsuj) and (frmverbot.tbsentencagensuj.value=gensuj)
    and (frmverbot.tbsentencanegativo.value=negat)
    and (frmverbot.tbsentencapessoa.value=pessoa) and (frmverbot.tbsentencagrau.value=grau)
    and (frmverbot.tbsentencatempo.value=tempo) and (frmverbot.tbsentencatipo.value=tipo) then begin
       frmverbot.tbitemsentenca.first;
       e:=1;
       zero:=0;
       for d:=1 to frmverbot.tbitemsentenca.recordcount do begin
         if (frmverbot.tbitemsentencatipo.value=pals[d].tppal) then inc(e);
         if pals[d].tppal=0 then inc(zero);
         frmverbot.tbitemsentenca.next;
       end;
       if d=e then
       exist:=true;
    end;
    frmverbot.tbsentenca.next;
  end;
  incomp:=false;
  if zero>0 then procsent;
      if exist=false then begin
    frmverbot.tbsentenca.last;
    count:=frmverbot.tbsentencacod_estrutura.value+1;
    frmverbot.tbsentenca.insert;
    frmverbot.tbsentencacod_estrutura.value:=count;
    frmverbot.tbsentencatpsuj.value:=tpsuj;
    frmverbot.tbsentencagensuj.value:=gensuj;
    frmverbot.tbsentencagrau.value:=grau;
    frmverbot.tbsentencapessoa.value:=pessoa;
    frmverbot.tbsentencanegativo.value:=negat;
    frmverbot.tbsentencatempo.value:=tempo;
    frmverbot.tbsentencatipo.value:=tipo;
    frmverbot.tbsentenca.post;
    for d:=1 to npal do begin
    frmverbot.tbitemsentenca.insert;
    frmverbot.tbitemsentencacod_item.value:=d;
    frmverbot.tbitemsentencacod_sentenca.value:=count;
    frmverbot.tbitemsentencatipo.value:=pals[d].tppal;
    frmverbot.tbitemsentencapal.value:=pals[d].pal;
    frmverbot.tbitemsentenca.post;
    end;

  end;
end;
end;


procedure procsent;
var
 c,d,p,c1,c2,tip:integer;
 incomp:boolean;
begin
  c:=0;
  tip:=0;
  with pessoas[pospes].sent[pessoas[pospes].nsent] do begin
    for d:=1 to npal do
      if pals[d].tppal=0 then begin
        incomp:=true;
        inc(c);
        p:=d;
    end;
    if c=1 then begin
      frmverbot.tbsentenca.first;
      c1:=0;
      while (not frmverbot.tbsentenca.eof) and (tip=0) do begin
        inc(c1);
        frmverbot.tbitemsentenca.first;
        c2:=1;
        while ((frmverbot.tbitemsentencatipo.value=pals[c2].tppal)
        or (p=frmverbot.tbitemsentencacod_item.value)) and (tip=0) and (not frmverbot.tbitemsentenca.eof)do
        begin
          frmverbot.tbitemsentenca.next;
          inc(c2);
          if (p=frmverbot.tbitemsentencacod_item.value) then tip:=frmverbot.tbitemsentencatipo.value;
          if (tip >=10) and (tip<=15) then tip:=0;
        end;
        frmverbot.tbsentenca.next;
      end;
      if tip<>0 then begin
        pals[p].tppal:=tip;
        if tip=palverbo then addverbo(pals[p].pal);
        if tip=paladj then addadjetivo(pals[p].pal);
        if tip=palverbo then addverbo(pals[p].pal);
        if tip=palsubst then addsubst(pals[p].pal);
        gravasentenca;
      end;
    end;
  end;
end;
end.
