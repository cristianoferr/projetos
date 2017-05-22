unit uentrada;
interface
uses   Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  Db, DBTables, StdCtrls, Grids, DBGrids;
const
 maxsent=20;
 maxpals=50;
 tpnone=0;
 tpexcl=1;
 tpinterrog=2;
 sujsimples=1;
 sujcomposto=2;
 genindef=0;
 genmasc=1;
 genfem=2;
 pes1=1;
 pes2=2;
 pes3=3;
 artdef=1;
 artindef=2;

 paladj=1;
 palsubst=2;
 palverbo=3;
 palartdef=4;
 palartindef=5;
 palconjaditiva=6;  //conjuncao aditiva
 palconjadversa=7; //conjucancao adversativa
 palconjconclusiva=8; //conjucancao conclusiva
 palconjexplicativa=9; //conjucancao explicativa
 palpronomepessoal=10;
 palpronomepossessivo=11;
 palpronomedemonstrativo=12;
 palpronomeindefinido=13;
 palpronomerelativo=14;
 palpronomeinterrogativo=15;
 palverboaux=16;
 palpreposicao=17;
 paladverbio=18;
 palintersurpresa=19;
 palinteranimacao=20;
 palinterchamado=21;
 palinterdesejo=22;
 palintermedo=23;
 palinteralivio=24;
 palinterruim=25;


 grauaumenta=1;
 graudiminui=2;
 grauigual=3;
 tempopresente=1;
 tempopreteritoimperfeito=2;
 tempopreteritoperfeito=3;
 tempopreteritomqperfeito=4;
 tempofuturopresente=5;
 tempofuturopreterito=6;
 tempopreteritoperfeitosimples=7;
 tempopreteritocomposto=8;

 type
   tpals=record
    pal:string;
    tppal:integer;
  end;
  tsent=record
    pals:array[1..maxpals] of tpals;
    subs,verbo,verboaux,adj:string;
    tipo,  {tpinterrog}
    npal,
    tpsuj, {sujcomposto}
    gensuj, {genfem;}
    pessoa, {1,2, ou 3}
    tpartigo, {artindef}
    grau,  {grauaumenta}
    tempo  {tempopresente}
    :integer;
    indicat,negat:boolean;
  end;
  tiposent=array[1..maxsent] of tsent;
  procedure pessoa(var pal:tpals);
  procedure ispronome(var pal:tpals);
  procedure diversos(var pal:tpals);
  function palavra:tpals;
  procedure verboaux(var pal:tpals);
  procedure settppal(i:integer);
  function gettppal:integer;
  function gettppalb(i:integer):integer;
  function getpal:string;
  procedure addverbo(s:string);
  procedure addaverbo(s,verbo:string;tempo,pessoa,tpsuj:integer;indic:boolean);
  procedure addsubst(s:string);
  function last(s,s2:string):boolean;
  function isin(s:string):boolean;
  procedure abreviacao(var pal:tpals);
      function getverbo(s:string):string;
    procedure separa(pal:string);
    procedure addpal(pal:string);
    procedure limpa(var pal:string);
    function islixo(c:char):boolean;
    procedure changesent(c:char);
    function desnominal(s:string;tpgen,tpnum:integer):string;
    procedure sufixo(var pal:tpals);
    procedure conjuncao;
    procedure listasent;
    function hasverbo(s:string):boolean;
    function hasaverbo(s:string):boolean;
    function gettipopal(i:integer):string;
    procedure substantivo(var pal:tpals);
    procedure adjetivo(var pal:tpals);
    procedure grau(var pal:tpals);
    procedure adverbio(var pal:tpals);
    procedure preposicao(var pal:tpals);
    procedure artigo(var pal:tpals);
    function soundex(s1,s2:string):integer;
    procedure entrada(s:string);
    procedure limpasent;
    procedure eliminaverbo(verbo,term:string);
    procedure addadjetivo(s:string);
    procedure verbodb(var pal:tpals);
    procedure interjeicao(var pal:tpals);



implementation
uses uverbot;
procedure entrada(s:string);
begin
frmverbot.init;
limpasent;
separa(s);
conjuncao;
listasent;

end;
procedure eliminaverbo(verbo,term:string);
var
  c:integer;
  v:string;
begin
v:=verbo;
if term='ar' then verbo[length(verbo)-1]:='a';
if term='er' then verbo[length(verbo)-1]:='e';
if term='ir' then verbo[length(verbo)-1]:='i';
frmverbot.tbverbo.setkey;
frmverbot.tbverboitem.value:=verbo;
if frmverbot.tbverbo.gotokey then
  frmverbot.tbverbo.delete;

  frmverbot.tbaverbo.first;
  while not frmverbot.tbaverbo.eof do begin
    if frmverbot.tbaverboverbo.value=verbo then begin
      frmverbot.tbaverbo.edit;
      frmverbot.tbaverboverbo.value:=v;
      frmverbot.tbaverbo.post;
    end;
    frmverbot.tbaverbo.next;

  end;
end;
procedure verbodb(var pal:tpals);
var
  fim,term,s:string;
  indic,found,certo:boolean;
  tempo,pessoa,tpsuj,c:integer;
begin
  verboaux(pal);
  frmverbot.tbconjug.first;
  found:=false;
  certo:=false;
  fim:='';
  if (gettppal=0) and (gettppalb(1)<>palpronomepossessivo) and (gettppalb(1)<>palpronomedemonstrativo) then begin
  for c:=1 to frmverbot.tbconjug.recordcount do begin
     if (last(frmverbot.tbconjugfim_conj.value,pal.pal)) then
     if length(fim)<length(frmverbot.tbconjugfim_conj.value) then
     if length(pal.pal)>3 then
     if //(length(frmverbot.tbconjugfim_conj.value)>2) or
      (gettppalb(1)<>palverbo) and (gettppalb(1)<>palpronomedemonstrativo)
      and (gettppalb(1)<>palpronomeindefinido)
      and (gettppalb(1)<>palpronomepossessivo)
  //   or ((length(frmverbot.tbconjugfim_conj.value)<3)
     //and ((sent[nsent].pessoa>0) or (sent[nsent].pessoa=frmverbot.tbconjugpessoa_conj.value)))
     then
     begin
       fim:=frmverbot.tbconjugfim_conj.value;
       term:=frmverbot.tbconjugterm_conj.value;
       tempo:=frmverbot.tbconjugtempo_conj.value;
       pessoa:=frmverbot.tbconjugpessoa_conj.value;
       tpsuj:=frmverbot.tbconjugtpsuj_conj.value;
       indic:=frmverbot.tbconjugindic_conj.value;
       certo:=frmverbot.tbconjugcerto_conj.value;
       found:=true;
     end;
  frmverbot.tbconjug.next;
  end;
  if found then begin
    s:=pal.pal;
    delete(pal.pal,length(pal.pal)-length(fim)+1,length(fim));
    pal.pal:=pal.pal+term;
    if tpsuj<>0 then sent[nsent].tpsuj:=tpsuj;
    if tempo<>0 then sent[nsent].tempo:=tempo;
    if pessoa<>0 then sent[nsent].pessoa:=pessoa;
    sent[nsent].indicat:=indic;
    settppal(palverbo);
    if certo then begin
    if term<>'ar' then eliminaverbo(pal.pal,'ar');
    if term<>'er' then eliminaverbo(pal.pal,'er');
    if term<>'ir' then eliminaverbo(pal.pal,'ir');
    end;
    addaverbo(s,pal.pal,tempo,pessoa,tpsuj,indic);
  end;
 if gettppal=palverbo then begin
   addverbo(pal.pal);
 end;
 if gettppal=0 then
 if length(pal.pal)>3 then
 if hasaverbo(pal.pal)=true then begin
   settppal(palverbo);
   pal.pal:=frmverbot.tbaverboverbo.value;
   tempo:=frmverbot.tbaverbotempo_verb.value;
   pessoa:=frmverbot.tbaverbopessoa_verb.value;
   tpsuj:=frmverbot.tbaverbotpsuj_verb.value;
   indic:=frmverbot.tbaverboindic_verb.value;
   if tpsuj<>0 then sent[nsent].tpsuj:=tpsuj;
   if tempo<>0 then sent[nsent].tempo:=tempo;
   if pessoa<>0 then sent[nsent].pessoa:=pessoa;
   sent[nsent].indicat:=indic;
 end;

 if gettppal=0 then
 if hasverbo(pal.pal)=true then
 settppal(palverbo);
 sent[nsent].pals[sent[nsent].npal].pal:=pal.pal;
end;
end;
function gettipopal(i:integer):string;
var
  s:string;
begin
case i of
1: s:='adj';
2: s:='subst';
3: s:='verbo';
4: s:='artdef';
5: s:='artindef';
6: s:='conjaditiva';  //conjuncao aditiva
7: s:='conjadversa'; //conjucancao adversativa
8: s:='conjconclusiva'; //conjucancao conclusiva
9: s:='conjexplicativa'; //conjucancao explicativa
10: s:='pronpessoal';
11: s:='pronpossessivo';
12: s:='prondemonstrativo';
13: s:='pronindefinido';
14: s:='pronrelativo';
15: s:='proninterrogativo';
16: s:='verboaux';
17: s:='prepos';
end;

gettipopal:=s;
end;


procedure limpasent;
var c,p:integer;
begin
nsent:=1;
for c:=1 to maxsent do begin
  sent[c].tpsuj:=0;
  sent[c].grau:=0;
  sent[c].gensuj:=0;
  sent[c].pessoa:=0;
  sent[c].tpartigo:=0;
  sent[c].tempo:=0;
  for p:=1 to maxpals do
    sent[c].pals[p].tppal:=0;
end;
sent[1].npal:=0;
sent[2].npal:=0;
sent[3].npal:=0;
frmverbot.memo1.lines.clear;
end;
procedure preposicao(var pal:tpals);
begin
  if gettppal=0 then
  with pal do
    if (pal='ante') or (pal='apos') or (pal='ate') or
     (pal='ateh') or (pal='com') or (pal='contra') or (pal='de') or
     (pal='desde') or (pal='em') or (pal='entre') or (pal='para') or
     (pal='per') or (pal='perante') or (pal='por') or (pal='sem') or (pal='soba') or
     (pal='sobre') or (pal='tras') then
       settppal(palpreposicao);
end;
procedure abreviacao(var pal:tpals);
begin
  with pal do begin
    frmverbot.tbabrev.setkey;
    frmverbot.tbabrevitem.value:=pal;
    if frmverbot.tbabrev.gotokey then begin
      pal:=frmverbot.tbabrevtroca1.value;
      if frmverbot.tbabrevpessoa.value>0 then sent[nsent].pessoa:=frmverbot.tbabrevpessoa.value;
      if frmverbot.tbabrevtempo.value>0 then sent[nsent].tempo:=frmverbot.tbabrevtempo.value;
      if frmverbot.tbabrevtpsuj.value>0 then sent[nsent].tpsuj:=frmverbot.tbabrevtpsuj.value;

    end;
  end;
  sent[nsent].pals[sent[nsent].npal]:=pal;
end;

procedure pessoa(var pal:tpals);
var
 c:integer;
begin
with pal do begin
  if pal='eu' then begin
    sent[nsent].tpsuj:=sujsimples;
    sent[nsent].pessoa:=1;
  end else
  if pal='voce' then begin
    sent[nsent].tpsuj:=sujsimples;
    sent[nsent].pessoa:=2;
    sent[nsent].gensuj:=0;
  end else
  if pal='tu' then begin
    sent[nsent].tpsuj:=sujsimples;
    sent[nsent].pessoa:=2;
  end else
  if pal='ele' then begin
    sent[nsent].tpsuj:=sujsimples;
    sent[nsent].pessoa:=3;
    sent[nsent].gensuj:=genmasc;
  end else
  if pal='ela' then begin
    sent[nsent].tpsuj:=sujsimples;
    sent[nsent].pessoa:=3;
    sent[nsent].gensuj:=genfem;
  end else
  if pal='nos' then begin
    sent[nsent].tpsuj:=sujcomposto;
    sent[nsent].pessoa:=1;
  end else
  if pal='vos' then begin
    sent[nsent].tpsuj:=sujcomposto;
    sent[nsent].pessoa:=2;
  end else
  if pal='eles' then begin
    sent[nsent].tpsuj:=sujcomposto;
    sent[nsent].pessoa:=3;
    sent[nsent].gensuj:=genmasc;
  end else
  if pal='elas' then begin
    sent[nsent].tpsuj:=sujcomposto;
    sent[nsent].pessoa:=3;
    sent[nsent].gensuj:=genfem;
  end else

  if pal='vc' then begin
    sent[nsent].tpsuj:=sujsimples;
    sent[nsent].pessoa:=2;
    sent[nsent].gensuj:=0;
    pal:='voce';
  end;
  sent[nsent].pals[sent[nsent].npal].pal:=pal;
  end;
end;
procedure diversos(var pal:tpals);
var
  c:integer;
begin
  with pal do if gettppal=0 then begin
    if (length(pal)>3) and (pos('oes',pal)=length(pal)-2) then begin
      c:=pos('oes',pal);
      pal[c]:='a';
      pal[c+1]:='o';
      delete(pal,length(pal),1);
      sent[nsent].tpsuj:=sujcomposto;
      settppal(palsubst);
    end else

     if (length(pal)>3) and (pos('aes',pal)=length(pal)-2) then begin
      c:=pos('aes',pal);
      pal[c]:='a';
      pal[c+1]:='o';
      delete(pal,length(pal),1);
      sent[nsent].tpsuj:=sujcomposto;
      settppal(palsubst);
    end else
    if (length(pal)>3) and (pos('es',pal)=length(pal)-1) then begin
      c:=pos('es',pal);
//      pal[c]:='a';
//      pal[c+1]:='o';
      delete(pal,length(pal)-1,2);
      settppal(palsubst);
   end else
    if (length(pal)>3) and (pos('os',pal)=length(pal)-1) then begin
      c:=pos('os',pal);
//      pal[c]:='a';
//      pal[c+1]:='o';
      delete(pal,length(pal),1);
      settppal(palsubst);
   end else

   if (pal[length(pal)-1]='n') and (pal[length(pal)]='s') then begin
     delete(pal,length(pal),1);
     pal[length(pal)]:='m';
     sent[nsent].tpsuj:=sujcomposto;
     settppal(palsubst);
   end
   else
   if (length(pal)>3) and (pos('ais',pal)=length(pal)-2) then begin
     delete(pal,length(pal),1);
     pal[length(pal)]:='l';
     sent[nsent].tpsuj:=sujcomposto;
     settppal(paladj);
   end else
   if (length(pal)>3) and (pos('eis',pal)=length(pal)-2) then begin
     delete(pal,length(pal),1);
     pal[length(pal)]:='l';
     sent[nsent].tpsuj:=sujcomposto;
     settppal(paladj);
   end
   else
   if (length(pal)>3) and (pos('ois',pal)=length(pal)-2) then begin
     delete(pal,length(pal),1);
     pal[length(pal)]:='l';
     sent[nsent].tpsuj:=sujcomposto;
     settppal(paladj);
   end
   else
   if (length(pal)>3) and (pos('uis',pal)=length(pal)-2) then begin
     delete(pal,length(pal),1);
     pal[length(pal)]:='l';
     sent[nsent].tpsuj:=sujcomposto;
     settppal(paladj);
   end;
//   pal:=sufixo(pal);


    if (length(pal)>4) and (pos('acho',pal)=length(pal)-3) then begin
      c:=pos('acho',pal);
      delete(pal,c,length(pal));
      pal:=pal+'o';
      settppal(palsubst);
      sent[nsent].grau:=graudiminui;
    end else
    if (length(pal)>3) and (pos('eta',pal)=length(pal)-2) then begin
      c:=pos('eta',pal);
      delete(pal,c,length(pal));
      pal:=pal+'a';
      settppal(palsubst);
      sent[nsent].grau:=graudiminui;
    end else
    if (length(pal)>3) and (pos('eto',pal)=length(pal)-2) then begin
      c:=pos('eto',pal);
      delete(pal,c,length(pal));
      pal:=pal+'o';
      settppal(palsubst);
      sent[nsent].grau:=graudiminui;
    end else
    if (length(pal)>4) and (pos('ote',pal)=length(pal)-2) then begin
      c:=pos('ote',pal);
      delete(pal,c,length(pal));
      pal:=pal+'o';
      settppal(palsubst);
      sent[nsent].grau:=graudiminui;
    end else
    if (length(pal)>4) and (pos('idao',pal)=length(pal)-3) then begin
      c:=pos('idao',pal);
      delete(pal,c,length(pal));
      pal:=pal+'o';
      settppal(paladj);
      sent[nsent].grau:=grauaumenta;
    end else
    if (length(pal)>3) and (pos('cao',pal)=length(pal)-2) then begin
      c:=pos('cao',pal);
      delete(pal,c,length(pal));
      pal:=pal+'r';
      settppal(palsubst);
      sent[nsent].grau:=grauaumenta;
    end else
    if (length(pal)>3) and (pos('ejo',pal)=length(pal)-2) then begin
      c:=pos('ejo',pal);
      delete(pal,c,length(pal));
//      pal:=pal+'o';
      settppal(palsubst);
      sent[nsent].grau:=graudiminui;
    end;

    if gettppal=palverbo then begin
      addverbo(pal);
    end;
    if gettppal=palsubst then begin
      addsubst(pal);
    end;
  end;
end;
function palavra:tpals;
begin
palavra:=sent[nsent].pals[sent[nsent].npal];
end;
procedure verboaux(var pal:tpals);
begin
  with pal do
  if (gettppal=0) or (gettppal=palverboaux) then 
  begin
    if pal='sou' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=1;
    end
    else
    if pal='estou' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='tenho' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=1;
    end
    else
    if pal='hei' then begin
      settppal(palverboaux);
      pal:='ha';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=1;
    end
    else
    if pal='es' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=2;
    end
    else
    if pal='estas' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='tens' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='eh' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
//      sent[nsent].pessoa:=3;
    end
    else
   if pal='esta' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='tem' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
//      sent[nsent].pessoa:=3;
    end
    else
   if pal='ha' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='somos' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='estamos' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='temos' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='havemos' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='sois' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='estais' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='tendes' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='haveis' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='sao' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='estao' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='tem' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
//      sent[nsent].pessoa:=1;
    end
    else
   if pal='hao' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='era' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='estava' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoimperfeito;
    end
    else
   if pal='tinha' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoimperfeito;
    end
    else
   if pal='havia' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoimperfeito;
    end
    else
   if pal='eras' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='estavas' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='tinhas' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='havias' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='eramos' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='estavamos' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='tinhamos' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='haviamos' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='eram' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='estavam' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='tinham' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='haviam' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='fui' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='estive' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='tive' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='houve' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
 //     sent[nsent].pessoa:=1;
    end
    else
   if pal='foste' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='estiveste' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='tiveste' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='houveste' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='foi' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='esteve' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='teve' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='houve' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='fomos' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='estivemos' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='tivemos' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='houvemos' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='fostes' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='estivestes' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='tivestes' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='houvestes' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='foram' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='estiveram' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='tiveram' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='houveram' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoperfeitosimples;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='fora' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritomqperfeito;
//      sent[nsent].pessoa:=1;
    end
    else
   if pal='estivera' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritomqperfeito;
//      sent[nsent].pessoa:=1;
    end
    else
   if pal='tivera' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritomqperfeito;
//      sent[nsent].pessoa:=1;
    end
    else
   if pal='houvera' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritomqperfeito;
//      sent[nsent].pessoa:=1;
    end
    else
   if pal='foras' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritomqperfeito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='estiveras' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritomqperfeito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='tiveras' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritomqperfeito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='houveras' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritomqperfeito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='foramos' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritomqperfeito;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='estiveramos' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritomqperfeito;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='tiveramos' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritomqperfeito;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='houveramos' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritomqperfeito;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='foreis' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritomqperfeito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='estivereis' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritomqperfeito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='tivereis' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritomqperfeito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='houvereis' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritomqperfeito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='foram' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritomqperfeito;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='estiveram' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritomqperfeito;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='tiveram' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritomqperfeito;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='houveram' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritomqperfeito;
      sent[nsent].pessoa:=3;
    end else
   if pal='serei' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='estarei' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='terei' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='haverei' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=1;
    end else
   if pal='seras' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='estaras' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='teras' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='haveras' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=2;
    end else
   if pal='sera' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='estara' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='tera' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='havera' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=3;
    end else
   if pal='seremos' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='estaremos' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='teremos' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='haveremos' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=1;
    end else
   if pal='sereis' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='estareis' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='tereis' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='havereis' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=2;
    end else
   if pal='serao' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='estarao' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='terao' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='haverao' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopresente;
      sent[nsent].pessoa:=3;
    end else
   if pal='seria' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopreterito;
//      sent[nsent].pessoa:=1;
    end
    else
   if pal='estaria' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopreterito;
//      sent[nsent].pessoa:=1;
    end
    else
   if pal='teria' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopreterito;
//      sent[nsent].pessoa:=1;
    end
    else
   if pal='haveria' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopreterito;
//      sent[nsent].pessoa:=1;
    end else
   if pal='serias' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopreterito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='estarias' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopreterito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='terias' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopreterito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='haverias' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempofuturopreterito;
      sent[nsent].pessoa:=2;
    end else
   if pal='seriamos' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopreterito;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='estariamos' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopreterito;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='teriamos' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopreterito;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='haveriamos' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopreterito;
      sent[nsent].pessoa:=1;
    end else
   if pal='seriam' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopreterito;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='estariam' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopreterito;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='teriam' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopreterito;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='haveriam' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempofuturopreterito;
      sent[nsent].pessoa:=3;
    end else
    if (isin('ter')) and (pal='sido') then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tempo:=tempopreteritocomposto;
    end
    else
    if (isin('ter')) and (pal='estado') then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tempo:=tempopreteritocomposto;
    end
    else
    if (isin('ter')) and (pal='tido') then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tempo:=tempopreteritocomposto;
    end
    else
    if (isin('ter')) and (pal='havido') then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tempo:=tempopreteritocomposto;
    end
//Modo subjuntivo
else
   if pal='seja' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
//      sent[nsent].pessoa:=1;
    end
    else
   if pal='esteja' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
//      sent[nsent].pessoa:=1;
    end
    else
   if pal='tenha' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
//      sent[nsent].pessoa:=1;
    end
    else
   if pal='haja' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
//      sent[nsent].pessoa:=1;
    end else
   if pal='sejas' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='estejas' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='tenhas' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='hajas' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=2;
    end else
   if pal='sejamos' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='estejamos' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='tenhamos' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='hajamos' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=1;
    end else
   if pal='sejais' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='estejais' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='tenhais' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='hajais' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=2;
    end else
   if pal='sejam' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='estejam' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='tenham' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='hajam' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopresente;
      sent[nsent].pessoa:=3;
    end else
   if pal='fosse' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoimperfeito;
//      sent[nsent].pessoa:=1;
    end
    else
   if pal='estivesse' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoimperfeito;
//      sent[nsent].pessoa:=1;
    end
    else
   if pal='tivesse' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoimperfeito;
//      sent[nsent].pessoa:=1;
    end
    else
   if pal='houvesse' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoimperfeito;
//      sent[nsent].pessoa:=1;
    end else
   if pal='fosses' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='estivesses' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='tivesses' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=2;
    end
    else
   if pal='houvesses' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujsimples;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=2;
    end else
   if pal='fossemos' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='estivessemos' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='tivessemos' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=1;
    end
    else
   if pal='houvessemos' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=1;
    end else
   if pal='fossem' then begin
      settppal(palverboaux);
      pal:='ser';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='estivessem' then begin
      settppal(palverboaux);
      pal:='estar';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='tivessem' then begin
      settppal(palverboaux);
      pal:='ter';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=3;
    end
    else
   if pal='houvessem' then begin
      settppal(palverboaux);
      pal:='haver';
      sent[nsent].tpsuj:=sujcomposto;
      sent[nsent].tempo:=tempopreteritoimperfeito;
      sent[nsent].pessoa:=3;
    end else
   if (isin('se')) then begin
     if pal='for' then begin
       pal:='ser';
       sent[nsent].tpsuj:=sujsimples;
     end else
     if pal='fores' then begin
       pal:='ser';
       sent[nsent].tpsuj:=sujsimples;
       sent[nsent].pessoa:=3;
     end else
     if pal='formos' then begin
     pal:='ser';
     sent[nsent].tpsuj:=sujcomposto;
     sent[nsent].pessoa:=1;
     end else
     if pal='forem' then begin
     pal:='ser';
     sent[nsent].tpsuj:=sujcomposto;
     sent[nsent].pessoa:=3;
     end else

     if pal='estiver' then begin
       pal:='estar';
       sent[nsent].tpsuj:=sujsimples;
     end else
     if pal='estiveres' then begin
       pal:='estar';
       sent[nsent].tpsuj:=sujsimples;
       sent[nsent].pessoa:=3;
     end else
     if pal='estivermos' then begin
     pal:='estar';
     sent[nsent].tpsuj:=sujcomposto;
     sent[nsent].pessoa:=1;
     end else
     if pal='estiverem' then begin
     pal:='estar';
     sent[nsent].tpsuj:=sujcomposto;
     sent[nsent].pessoa:=3;
     end else

     if pal='tiver' then begin
       pal:='ter';
       sent[nsent].tpsuj:=sujsimples;
     end else
     if pal='tiveres' then begin
       pal:='ter';
       sent[nsent].tpsuj:=sujsimples;
       sent[nsent].pessoa:=3;
     end else
     if pal='tivermos' then begin
     pal:='ter';
     sent[nsent].tpsuj:=sujcomposto;
     sent[nsent].pessoa:=1;
     end else
     if pal='tiverem' then begin
     pal:='ter';
     sent[nsent].tpsuj:=sujcomposto;
     sent[nsent].pessoa:=3;
     end else

     if pal='houver' then begin
       pal:='haver';
       sent[nsent].tpsuj:=sujsimples;
     end else
     if pal='houveres' then begin
       pal:='haver';
       sent[nsent].tpsuj:=sujsimples;
       sent[nsent].pessoa:=3;
     end else
     if pal='houvermos' then begin
     pal:='haver';
     sent[nsent].tpsuj:=sujcomposto;
     sent[nsent].pessoa:=1;
     end else
     if pal='houverem' then begin
     pal:='haver';
     sent[nsent].tpsuj:=sujcomposto;
     sent[nsent].pessoa:=3;
     end else

     if pal='tiver' then begin
       pal:='ter';
       sent[nsent].tpsuj:=sujsimples;
     end else
     if pal='tiveres' then begin
       pal:='ter';
       sent[nsent].tpsuj:=sujsimples;
       sent[nsent].pessoa:=3;
     end else
     if pal='tivermos' then begin
     pal:='ter';
     sent[nsent].tpsuj:=sujcomposto;
     sent[nsent].pessoa:=1;
     end else
     if pal='tiverem' then begin
     pal:='ter';
     sent[nsent].tpsuj:=sujcomposto;
     sent[nsent].pessoa:=3;
     end;
     if (pal='ser') or (pal='haver') or (pal='ter') or (pal='estar') then settppal(palverboaux);

   end;

  //deve ficar por ultimo
   if pal='sido' then begin
      settppal(palverboaux);
      pal:='ser';
    end
    else
   if pal='tido' then begin
      settppal(palverboaux);
      pal:='ter';
    end
    else
   if pal='havido' then begin
      settppal(palverboaux);
      pal:='haver';
    end; 



  end;
  sent[nsent].pals[sent[nsent].npal].pal:=pal.pal;
end;
procedure adverbio(var pal:tpals);
begin
  with pal do
  if (last('mente',pal)) and (length(pal)>5)  then begin
    delete(pal,length(pal)-4,5);
    if pal[length(pal)]='a' then pal[length(pal)]:='o';
    settppal(paladj);
    addadjetivo(pal);
  end;
end;
procedure adjetivo(var pal:tpals);
begin
  adverbio(pal);
  frmverbot.tbadj.setkey;
  frmverbot.tbadjitem.value:=pal.pal;
  if frmverbot.tbadj.gotokey then begin
    settppal(paladj);
    if frmverbot.tbadjgrau.value<>0 then 
      sent[nsent].grau:=frmverbot.tbadjgrau.value;
      if frmverbot.tbadjnegativo.value then sent[nsent].negat:=true;
//    if sent[nsent].gensuj=0 then sent[nsent].gensuj:=frmverbot.tbpronomegenero.value;
{    if sent[nsent].tpsuj=0 then begin
      if frmverbot.tbpronomesingular.value then sent[nsent].tpsuj:=sujsimples
      else sent[nsent].tpsuj:=sujcomposto;
  end;}

  end else begin
    if (last('iissimo',pal.pal)) then begin
       delete(pal.pal,length(pal.pal)-5,6);
       pal.pal:=pal.pal+'o';
       settppal(paladj);
       addadjetivo(pal.pal);
    end
    else
    if (last('issimo',pal.pal)) then begin
       delete(pal.pal,length(pal.pal)-4,5);
       pal.pal:=pal.pal+'o';
       settppal(paladj);
       addadjetivo(pal.pal);
    end
    else
    if (last('imo',pal.pal)) then begin
       delete(pal.pal,length(pal.pal)-2,3);
//                                                                      pal.pal:=pal.pal+'o';
       settppal(paladj);
       addadjetivo(pal.pal);
    end
    else

  end;
end;
procedure substantivo(var pal:tpals);
begin
  if gettppal=0 then begin
  frmverbot.tbsubst.setkey;
  frmverbot.tbsubstitem.value:=pal.pal;
  if frmverbot.tbsubst.gotokey then
    settppal(palsubst);
  end;
end;

procedure ispronome(var pal:tpals);
begin
  if (gettppal=0) then begin
  frmverbot.tbpronome.setkey;
  frmverbot.tbpronomeitem.value:=pal.pal;
  if frmverbot.tbpronome.gotokey then begin
    settppal(frmverbot.tbpronometppronome.value);
    if sent[nsent].gensuj=0 then sent[nsent].gensuj:=frmverbot.tbpronomegenero.value;
    if sent[nsent].pessoa=0 then sent[nsent].pessoa:=frmverbot.tbpronomepessoa.value;
    if sent[nsent].tpsuj=0 then begin
      if frmverbot.tbpronomesingular.value then sent[nsent].tpsuj:=sujsimples
      else sent[nsent].tpsuj:=sujcomposto;
  end;
  end;
  end;
end;
procedure settppal(i:integer);
begin
sent[nsent].pals[sent[nsent].npal].tppal:=i;
end;
function gettppal:integer;
begin
gettppal:=sent[nsent].pals[sent[nsent].npal].tppal;
end;
function gettppalb(i:integer):integer;
begin
gettppalb:=sent[nsent].pals[sent[nsent].npal-i].tppal;
end;

function getpal:string;
begin
  getpal:=sent[nsent].pals[sent[nsent].npal].pal;
end;
function hasverbo(s:string):boolean;
begin
  frmverbot.tbverbo.setkey;
  frmverbot.tbverboitem.value:=s;
  if frmverbot.tbverbo.gotokey then
  hasverbo:=true
  else hasverbo:=false;
end;
function hasaverbo(s:string):boolean;
begin
  frmverbot.tbaverbo.setkey;
  frmverbot.tbaverboitem.value:=s;
  if frmverbot.tbaverbo.gotokey then
  hasaverbo:=true
  else hasaverbo:=false;
end;

procedure addverbo(s:string);
begin
frmverbot.tbverbo.setkey;
frmverbot.tbverboitem.value:=s;
if (not frmverbot.tbverbo.gotokey) then begin
  frmverbot.tbverbo.insert;
  frmverbot.tbverboitem.value:=s;
  frmverbot.tbverbo.post;
end;
end;

procedure addaverbo(s,verbo:string;tempo,pessoa,tpsuj:integer;indic:boolean);

begin
frmverbot.tbaverbo.setkey;
frmverbot.tbaverboitem.value:=s;
if (not frmverbot.tbaverbo.gotokey) then begin
  frmverbot.tbaverbo.insert;
  frmverbot.tbaverboitem.value:=s;
  frmverbot.tbaverbotempo_verb.value:=tempo;
  frmverbot.tbaverbopessoa_verb.value:=pessoa;
  frmverbot.tbaverbotpsuj_verb.value:=tpsuj;
  frmverbot.tbaverboindic_verb.value:=indic;
  frmverbot.tbaverboverbo.value:=verbo;
  frmverbot.tbaverbo.post;
end;
end;

procedure addadjetivo(s:string);
begin
frmverbot.tbadj.setkey;
frmverbot.tbadjitem.value:=s;
if (not frmverbot.tbadj.gotokey) then begin
  frmverbot.tbadj.insert;
  frmverbot.tbadjitem.value:=s;
  frmverbot.tbadj.post;
end;
end;
procedure addsubst(s:string);
begin
frmverbot.tbsubst.setkey;
frmverbot.tbsubstitem.value:=s;
if (not frmverbot.tbsubst.gotokey) then begin
  frmverbot.tbsubst.insert;
  frmverbot.tbsubstitem.value:=s;
  frmverbot.tbsubst.post;
end;

end;
function last(s,s2:string):boolean;
begin
delete(s2,1,length(s2)-length(s));
last:=(s2=s);
end;
procedure interjeicao(var pal:tpals);
begin
  with pal do begin
  if (pal='oh') or (pal='chi') or (pal='xi') or (pal='uai') or (pal='ah') or (pal='caramba') or (pal='oba') or (pal='urra') or
  ((pal='vida') and (isin('puxa')) or
  ((pal='deus') and (isin('meu')) or
  ((pal='ora') and (isin('essa'))))) then
     settppal(palintersurpresa);
    if (pal='ei') or (pal='psiu') or (pal='alo') or (pal='ola') or (pal='oi') then
     settppal(palinterchamado);
   if (pal='eia') or (pal='avante') then
    settppal(palinteranimacao);

  end;
end;
function isin(s:string):boolean;
var
 c:integer;
begin
  result:=false;
  for c:=1 to sent[nsent].npal do
    if sent[nsent].pals[c].pal=s then result:=true;
end;
procedure grau(var pal:tpals);
var
  c:integer;
begin
  with pal do
  if (gettppal=0) or (gettppal=palsubst) then begin
  if (length(pal)>5) and (pos('alhao',pal)=length(pal)-4) then begin
    c:=pos('alhao',pal);
    delete(pal,c,length(pal));
    pal:=pal+'a';
    sent[nsent].grau:=grauaumenta;
    settppal(palsubst);
  end else
  if (length(pal)>6) and (pos('sarrao',pal)=length(pal)-5) then begin
    c:=pos('sarrao',pal);
    delete(pal,c,length(pal));
//    pal:=pal+'a';
    if pal[length(pal)]='n' then pal[length(pal)]:='m';
    sent[nsent].grau:=grauaumenta;
    settppal(palsubst);
  end else
  if (length(pal)>6) and (pos('arrao',pal)=length(pal)-4) then begin
    c:=pos('arrao',pal);
    delete(pal,c+1,length(pal));
//    pal:=pal+'a';
    if pal[length(pal)]='a' then pal[length(pal)]:='o';
    sent[nsent].grau:=grauaumenta;
    settppal(palsubst);
  end else
  if (length(pal)>6) and (pos('eirao',pal)=length(pal)-4) then begin
    c:=pos('eirao',pal);
    delete(pal,c,length(pal));
    sent[nsent].grau:=grauaumenta;
    settppal(palsubst);
  end else
  if (length(pal)>4) and (pos('sao',pal)=length(pal)-2) then begin
    c:=pos('sao',pal);
    delete(pal,c,length(pal));
    sent[nsent].grau:=grauaumenta;
    settppal(palsubst);
  end else
  if (length(pal)>4) and (pos('ona',pal)=length(pal)-2) then begin
    c:=pos('ona',pal);
    delete(pal,c,length(pal));
    sent[nsent].grau:=grauaumenta;
    settppal(palsubst);
  end else
  if (length(pal)>4) and (pos('arao',pal)=length(pal)-3) then begin
    c:=pos('arao',pal);
    delete(pal,c,length(pal));
    pal:=pal+'a';
    sent[nsent].grau:=grauaumenta;
    settppal(palsubst);
  end else
    if (length(pal)>5) and (pos('sinho',pal)=length(pal)-4) then begin
    c:=pos('sinho',pal);
    delete(pal,c,length(pal));
//    pal[length(pal)]:='o';
    sent[nsent].grau:=graudiminui;
    settppal(palsubst);
  end else
    if (length(pal)>5) and (pos('sinha',pal)=length(pal)-4) then begin
    c:=pos('sinha',pal);
    delete(pal,c,length(pal));
//    pal[length(pal)]:='o';
    sent[nsent].grau:=graudiminui;
    settppal(palsubst);
  end else
    if (length(pal)>5) and (pos('sinhos',pal)=length(pal)-5) then begin
    c:=pos('sinhos',pal);
    delete(pal,c,length(pal));
//    pal[length(pal)]:='o';
    sent[nsent].grau:=graudiminui;
    sent[nsent].tpsuj:=sujcomposto;
    settppal(palsubst);
  end else
    if (length(pal)>5) and (pos('sinhas',pal)=length(pal)-5) then begin
    c:=pos('sinhas',pal);
    delete(pal,c,length(pal));
//    pal[length(pal)]:='o';
    sent[nsent].grau:=graudiminui;
    sent[nsent].tpsuj:=sujcomposto;
    settppal(palsubst);
  end else

  if (length(pal)>4) and (pos('inho',pal)=length(pal)-3) then begin
    c:=pos('inho',pal);
    delete(pal,c+1,length(pal));
    pal[length(pal)]:='o';
    sent[nsent].grau:=graudiminui;
    settppal(palsubst);
  end;


  if (pal='quanto') then begin
    sent[nsent].grau:=grauigual;
    settppal(paladj);
  end;
    if (pal='como') then begin
    sent[nsent].grau:=grauigual;
    settppal(paladj);
  end;
    if (pal='quao') then begin
    sent[nsent].grau:=grauigual;
    settppal(paladj);
  end;


  if (pal='mais') then begin
    sent[nsent].grau:=grauaumenta;
    settppal(paladj);
  end;
  if (pal='menos') then begin
    sent[nsent].grau:=graudiminui;
    settppal(paladj);
  end;
  if (pal='pequeno') then begin
    sent[nsent].grau:=graudiminui;
    settppal(paladj);
  end;
  if (pal='menor') then begin
    sent[nsent].grau:=graudiminui;
    settppal(paladj);
  end;


end;

end;

function desnominal(s:string;tpgen,tpnum:integer):string;
begin
delete(s,length(s),1);
if tpgen=genfem then s:=s+'a' else s:=s+'o';
if tpnum=sujcomposto then s:=s+'s';
desnominal:=s;
end;
procedure changesent(c:char);
begin
  if c='?' then sent[nsent].tipo:=tpinterrog;
  if c='!' then sent[nsent].tipo:=tpexcl;
  if c in ['!','?','.'] then inc(nsent);
end;

procedure artigo(var pal:tpals);
begin
with pal do
if pal='ela' then begin
    sent[nsent].tpsuj:=sujsimples;
    sent[nsent].gensuj:=genfem;
  end else
  if pal='ele' then begin
    sent[nsent].tpsuj:=sujsimples;
    sent[nsent].gensuj:=genmasc;
  end else
   if pal='um' then begin
    sent[nsent].tpsuj:=sujsimples;
    sent[nsent].gensuj:=genmasc;
    sent[nsent].tpartigo:=artindef;
    settppal(palartindef);
  end else
   if pal='uma' then begin
    sent[nsent].tpsuj:=sujsimples;
    sent[nsent].gensuj:=genfem;
    sent[nsent].tpartigo:=artindef;
    settppal(palartindef);
  end else
   if pal='ela' then begin
    sent[nsent].tpsuj:=sujsimples;
    sent[nsent].gensuj:=genmasc;
  end else
  if pal='elas' then begin
    pal:='ela';
    sent[nsent].tpsuj:=sujcomposto;
    sent[nsent].gensuj:=genfem;
  end else
  if pal='umas' then begin
    pal:='uma';
    sent[nsent].tpsuj:=sujcomposto;
    sent[nsent].gensuj:=genfem;
    sent[nsent].tpartigo:=artindef;
    settppal(palartindef);
  end else
    if pal='uns' then begin
    pal:='um';
    sent[nsent].tpsuj:=sujcomposto;
    sent[nsent].gensuj:=genmasc;
    sent[nsent].tpartigo:=artindef;
    settppal(palartindef);
  end else
 if pal='eles' then begin
    pal:='ele';
    sent[nsent].tpsuj:=sujcomposto;
    sent[nsent].gensuj:=genmasc;
end else
  if pal='as' then begin
    pal:='a';
    sent[nsent].tpsuj:=sujcomposto;
    sent[nsent].gensuj:=genfem;
    sent[nsent].tpartigo:=artdef;
    settppal(palartdef);
end else
  if pal='os' then begin
    pal:='o';
    sent[nsent].tpsuj:=sujcomposto;
    sent[nsent].gensuj:=genmasc;
    sent[nsent].tpartigo:=artdef;
    settppal(palartdef);
end;
end;

procedure separa(pal:string);
var
 c:integer;
begin

  pal:=lowercase(pal+' ');
  c:=1;
  limpa(pal);
  while length(pal)>1 do begin
  if (islixo(pal[c])) then
    if copy(pal,1,c-1)<>'' then begin
    addpal(lowercase(copy(pal,1,c-1)));
//    memo1.lines.add(getpal);
    changesent(pal[c]);
    delete(pal,1,c);
    c:=0;
  end;
  inc(c);
  end;
end;

procedure limpa(var pal:string);
var
 f:boolean;
 c:integer;
begin


repeat
f:=true;
c:=0;
while c<=length(pal) do
begin
  inc(c);
    if pal[c]='?' then begin
    delete(pal,c,1);
    sent[nsent].tipo:=tpinterrog;
  end else
    if pal[c]='!' then begin
    delete(pal,c,1);
    sent[nsent].tipo:=tpexcl;
  end;
  if pal[c]='ã' then pal[c]:='a';
  if pal[c]='à' then pal[c]:='a';
  if pal[c]='á' then pal[c]:='a';
  if pal[c]='õ' then pal[c]:='o';
  if pal[c]='ò' then pal[c]:='o';
  if pal[c]='ó' then pal[c]:='o';
  if pal[c]='ô' then pal[c]:='o';
  if pal[c]='ê' then pal[c]:='e';
  if pal[c]='é' then begin
    pal[c]:='e';
    if (pal[c+1]=' ') and (pal[c-1]=' ') then
    Insert('h', pal, c+1);
  end;
  if pal[c]='í' then pal[c]:='i';
  if pal[c]='î' then pal[c]:='i';
  if pal[c]='û' then pal[c]:='u';

  if pal[c]='ç' then pal[c]:='c';
  if pal[c]='z' then pal[c]:='s';  //Sei que eh errado trocar 'z' por 's' (e 'ss' por 's'
end;                               //Mas a maioria das pessoas erra nessas palavras, e de nada
                                   //adianta ter um programa que fale MELHOR do que a pessoa.




  if islixo(pal[c]) then begin
    inc(c);
    while islixo(pal[c]) do begin
      delete(pal,c,1);
      f:=false;
    end;
  end;



until f;
end;

function islixo(c:char):boolean;
begin
  if c='?' then begin
   sent[nsent].tipo:=tpinterrog;
  end else
    if c='!' then begin
    sent[nsent].tipo:=tpexcl;
  end;

  if (c=' ') or (c='.') or (c=',') or (c='!') or (c='?') then
    islixo:=true else islixo:=false;
end;

procedure addpal(pal:string);
var
c:integer;
begin

  if pal[1]=' ' then
  delete(pal,1,1);
  inc(sent[nsent].npal);
//  pal:=abreviacao(sent[nsent].pals[sent[nsent].npal]);
//  pal:=artigo(sent[nsent].pal);
  sent[nsent].pals[sent[nsent].npal].pal:=pal;
end;

procedure sufixo(var pal:tpals);
var
  c:integer;
begin
    with pal do
    if (length(pal)>5) and (pos('ulsao',pal)=length(pal)-4) then begin
      c:=pos('ulsao',pal);
      delete(pal,c,length(pal));
      pal:=pal+'elir';
      settppal(palverbo);
    end else
       if (length(pal)>4) and (pos('ncao',pal)=length(pal)-3) then begin
      c:=pos('ncao',pal);
      pal[c]:='r';
      delete(pal,c+1,c+3);
      settppal(palverbo);
    end else
    if (length(pal)>6) and (pos('cessao',pal)=length(pal)-5) then begin
      c:=pos('cessao',pal);
      delete(pal,c,length(pal));
      pal:=pal+'ceder';
      settppal(palverbo);
    end else
    if (length(pal)>7) and (pos('gressao',pal)=length(pal)-6) then begin
      c:=pos('gressao',pal);
      delete(pal,c,length(pal));
      pal:=pal+'gredir';
      settppal(palverbo);
    end else
    if (length(pal)>7) and (pos('pressao',pal)=length(pal)-6) then begin
      c:=pos('pressao',pal);
      delete(pal,c,length(pal));
      pal:=pal+'primir';
      settppal(palverbo);
    end else

    if (length(pal)>5) and (pos('issao',pal)=length(pal)-4) then begin
      c:=pos('issao',pal);
      delete(pal,c,length(pal));
      pal:=pal+'itir';
      settppal(palverbo);
    end else
    if (length(pal)>4) and (pos('sao',pal)=length(pal)-2) then begin
      c:=pos('sao',pal);
      delete(pal,c,length(pal));
      pal:=pal+'ter';
      settppal(palverbo);
    end;



end;

function getverbo(s:string):string;
var
  c:integer;
  r:string;
begin
  frmverbot.tbverbo.first;
  r:='';
  for c:=1 to frmverbot.tbverbo.recordcount do begin
    if soundex(s,r)<soundex(s,frmverbot.tbverboitem.value) then
      r:=frmverbot.tbverboitem.value;
    frmverbot.tbverbo.next;
  end;
  result:=r;
end;


procedure conjuncao;
var
 auxs:array[1..maxsent] of tsent;
 c,s,p,se,pa:integer;
 b:boolean;
begin
  for c:=1 to maxsent do
  begin
    auxs[c].npal:=0;
  end;
  se:=0;
  for c:=1 to nsent do begin
    inc(se);
    pa:=0;
    b:=false;
    for p:=1 to sent[c].npal do begin
      inc(pa);
      if not b then begin
        auxs[se].pals[pa].pal:=sent[c].pals[p].pal;
        auxs[se].pals[pa].tppal:=sent[c].pals[p].tppal;
      end;
      b:=false;
      if (sent[c].pals[p].pal='e') then begin
        settppal(palconjaditiva);
        auxs[se].npal:=pa;
      end;
      if (sent[c].pals[p].pal='mas') or
      (sent[c].pals[p].pal='nem') or
//      (sent[c].pals[p].pal='e') or
      (sent[c].pals[p].pal='senao') then begin
          auxs[se].npal:=pa;
          inc(se);
          pa:=0;
          if (sent[c].pals[p+1].pal ='ainda') then b:=true;
          if (sent[c].pals[p+1].pal ='tambem') then b:=true;
          if b then settppal(palconjaditiva);
          if sent[c].pals[p].pal='mas' then settppal(palconjadversa);

       end else
      if (sent[c].pals[p].pal='porem') or
      (sent[c].pals[p].pal='todavia') or
      (sent[c].pals[p].pal='entretando') or
      (sent[c].pals[p].pal='senao') or
      (sent[c].pals[p].pal='contudo') then begin
        auxs[se].npal:=pa;
        inc(se);
        pa:=0;
        settppal(palconjadversa);
       end  else
      if ((sent[c].pals[p].pal='no') and (sent[c].pals[p+1].pal='entanto')) or
         ((sent[c].pals[p].pal='nao') and (sent[c].pals[p+1].pal='obstante')) or
         ((sent[c].pals[p].pal='apesar') and (sent[c].pals[p+1].pal='disso')) or
         ((sent[c].pals[p].pal='em') and (sent[c].pals[p+1].pal='todo') and (sent[c].pals[p+1].pal='caso'))
       then begin
        auxs[se].npal:=pa;
        inc(se);
        pa:=0;
        settppal(palconjadversa);
        
        b:=true;
       end else
      if ((sent[c].pals[p].pal='logo')) or
         ((sent[c].pals[p].pal='portanto')) or
         ((sent[c].pals[p].pal='pois')) then begin
         auxs[se].npal:=pa;
        inc(se);
        pa:=0;
        settppal(palconjconclusiva);
      end else
      
      if ((sent[c].pals[p].pal='por') and (sent[c].pals[p+1].pal='conseguinte')) or
         ((sent[c].pals[p].pal='por') and (sent[c].pals[p+1].pal='isso')) then begin
        auxs[se].npal:=pa;
        inc(se);
        pa:=0;
        settppal(palconjconclusiva);
        b:=true;
       end;


        auxs[se].npal:=pa;
        if b then dec(pa);
    end;
  end;
for c:=1 to se do
for p:=1 to auxs[c].npal do
begin
  pessoa(auxs[c].pals[p]);
end;
for c:=1 to se do begin
for p:=1 to auxs[c].npal do
begin
  nsent:=c;
  sent[c].npal:=p;
  sent[c].pals[p].tppal:=auxs[c].pals[p].tppal;
  if sent[c].pals[p].pal='e' then sent[c].pals[p].tppal:=palconjaditiva;
  abreviacao(auxs[c].pals[p]);
  interjeicao(auxs[c].pals[p]);
  adjetivo(auxs[c].pals[p]);
  preposicao(auxs[c].pals[p]);
  ispronome(auxs[c].pals[p]);
  substantivo(auxs[c].pals[p]);
  verbodb(auxs[c].pals[p]);
  sufixo(auxs[c].pals[p]);
  artigo(auxs[c].pals[p]);
  grau(auxs[c].pals[p]);
  diversos(auxs[c].pals[p]);
end;
end;
nsent:=se;
end;


procedure listasent;
var
  c,p:integer;
begin
  for c:=1 to nsent do
    for p:=1 to sent[c].npal do
      frmverbot.memo1.lines.add(sent[c].pals[p].pal);
  frmverbot.label11.caption:=inttostr(nsent);
  frmverbot.esujeito.caption:=inttostr(sent[nsent].tpsuj);
  frmverbot.egenero.caption:=inttostr(sent[nsent].gensuj);
  frmverbot.egrau.caption:=inttostr(sent[nsent].grau);
  frmverbot.etempo.caption:=inttostr(sent[nsent].tempo);
  frmverbot.eartigo.caption:=inttostr(sent[nsent].tpartigo);
  frmverbot.epessoa.caption:=inttostr(sent[nsent].pessoa);
  frmverbot.etipo.caption:=inttostr(sent[nsent].tipo);
  if sent[nsent].negat then
  frmverbot.enegat.caption:='True' else
  frmverbot.enegat.caption:='False';

  end;
function soundex(s1,s2:string):integer;
var
  c1,c2:integer;
  r:integer;
begin
  r:=0;
  for c1:=1 to length(s1) do
    for c2:=1 to length(s2) do
      if s1[c1]=s2[c2] then inc(r);
  soundex:=r;
end;

end.
