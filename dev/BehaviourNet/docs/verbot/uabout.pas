unit uabout;
{
This module was made by Cristiano M. Ferreira,
If you use this module for anything... please let me know.
You may email me if you have any question.
lebeau@linuxbr.com.br
or visit my page at http://lbworld.cjb.net
}

interface
uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  StdCtrls, math,ExtCtrls;

const
  tam=3;
  maxpt=40;
  maxdirs=32;
  maxp=40;
  incang=360 div maxdirs;
type
  tponto=record
    x,y,ang,pt,toang:integer;
    cor:longint;
  end;
  tp=record
  pt:array[1..maxpt] of tponto;
  end;
  Tfrmabout = class(TForm)
    Label1: TLabel;
    Label2: TLabel;
    Label3: TLabel;
    Button1: TButton;
    Panel1: TPanel;
    tab: TImage;
    Label4: TLabel;
    Label5: TLabel;
    Timer1: TTimer;
          Function gety(c:integer):integer;
    Function getx(c:integer):integer;
    procedure move(c:integer);
    procedure inicializa;
    procedure move2(c:integer);
    procedure move3(c:integer);
    procedure Timer1Timer(Sender: TObject);
    procedure FormCreate(Sender: TObject);
    procedure Button1Click(Sender: TObject);
    function AngleOfPt(x1,y1,x2,y2:integer) : integer;
    procedure tabMouseMove(Sender: TObject; Shift: TShiftState; X,
      Y: Integer);
    procedure FormClose(Sender: TObject; var Action: TCloseAction);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  frmabout: Tfrmabout;
    p:array[1..maxp] of tp;
  posi,ptpos:integer;
  gx,gy,gang:integer;


implementation

{$R *.DFM}
Function tfrmabout.getx(c:integer):integer;
var
  radians:double;
begin
   Radians := degtorad(p[posi].pt[c-1].Ang);
   getx := p[posi].pt[c-1].x+Round(tam * Cos(Radians));
end;
Function tfrmabout.gety(c:integer):integer;
var                       
  radians:double;
begin
   Radians := degtorad(p[posi].pt[c-1].Ang);
   gety := p[posi].pt[c-1].y+Round(tam * sin(Radians));
end;

procedure tfrmabout.inicializa;
var
  c,s:integer;
begin
  ptpos:=2;
  gang:=100;
  gx:=20+random(tab.width-20);
  gy:=20+random(tab.height-20);
  for s:=1 to maxp do begin
//  randomize;
  posi:=s;
  if s=1 then p[posi].pt[1].cor:=clred;
  if s=2 then p[posi].pt[1].cor:=clgreen;
  if s=3 then p[posi].pt[1].cor:=clblack;
  if s=4 then p[posi].pt[1].cor:=clblue;
  p[posi].pt[1].ang:=random(360);
  p[posi].pt[1].x:=20+random(tab.width-40);
  p[posi].pt[1].y:=20+random(tab.height-40);
//  randomize;
  for c:=2 to maxpt do begin
    p[posi].pt[c].ang:=random(360);
    p[posi].pt[c].x:=getx(c);
    p[posi].pt[c].y:=gety(c);
  end;
  end;
end;
procedure tfrmabout.move2(c:integer);
var
  i,x,xx:integer;
begin
{  for i:=2 to maxpt do begin
    p[posi].pt[i].ang:=p[posi].pt[i-1].ang;
  end;}
{  x=90
  xx=350
  xx-x=260
  x->xx
  x-350+360=100}
  x:=p[posi].pt[c].ang;
  xx:=p[posi].pt[c-1].ang;
  if (x>xx) and (abs(x-xx+360)<abs(xx-x)) then
  dec(p[posi].pt[c].ang,incang) else
  inc(p[posi].pt[c].ang,incang);

  if p[posi].pt[c].ang>360 then p[posi].pt[c].ang:=p[posi].pt[c].ang-360;
  if p[posi].pt[c].ang<0 then p[posi].pt[c].ang:=360+p[posi].pt[c].ang;

end;
procedure Tfrmabout.Timer1Timer(Sender: TObject);
var
  c,s:integer;
begin
  tab.Canvas.FillRect(ClientRect);
  for s:=1 to maxp do begin
  posi:=s;
  for c:=2 to maxpt do begin
  tab.canvas.pen.color:=p[posi].pt[1].cor;
//  canvas.brush.color:=clblack;
//  Canvas.FillRect(ClientRect);
  tab.Canvas.MoveTo(p[posi].pt[c-1].x, p[posi].pt[c-1].y);
  tab.Canvas.LineTo(p[posi].pt[c].x, p[posi].pt[c].y);
  end;
  for c:=1 to maxpt do begin
move3(c);
//  end;
  if (c>1) then begin
  p[posi].pt[c].x:=getx(c);
  posi:=s;
  p[posi].pt[c].y:=gety(c);
  end;
  end;
end;
{  if posi=3 then
  if ptposi<maxpt then
    move2(ptposi)
  else move(ptposi);
  if posi=1 then
  if ptposi>1 then
  move2(ptposi)
  else move(ptposi);
  inc(ptposi);
  if ptposi>maxpt then ptposi:=1;
}
 { if (posi>1) and (posi<>3) and (posi<>4) then
  move(c) else
  if posi=4 then }{ else
  begin
    if c=1 then
    begin
//    move(c);
{    if gx>p[posi].pt[1].x then
    inc(p[posi].pt[1].x) else
    if gx<p[posi].pt[1].x then
    dec(p[posi].pt[1].x) else
    gx:=random(width);
    if gy>p[posi].pt[1].y then
    inc(p[posi].pt[1].y) else
    if gy<p[posi].pt[1].y then
    dec(p[posi].pt[1].y) else
    gy:=random(height);}
//    p[posi].pt[1].ang:=AngleOfPt(p[posi].pt[1].x,p[posi].pt[1].y,gx,gy);

//  end;

//  end;
//  end;
end;
procedure tfrmabout.move(c:integer);
begin
  if abs(abs(p[posi].pt[c].toang)-(abs(p[posi].pt[c].ang)))>incang then begin
  if p[posi].pt[c].toang>p[posi].pt[c].ang then
  inc(p[posi].pt[c].ang,incang) else
  dec(p[posi].pt[c].ang,incang);
  end
  else
    p[posi].pt[c].toang:=random(360);

end;
procedure tfrmabout.move3(c:integer);
var
  x,xx:integer;
begin
{if abs(abs(p[posi].pt[c].toang)-(abs(p[posi].pt[c].ang)))>incang then begin
  x:=p[posi].pt[c].ang;
  xx:=p[posi].pt[c-1].ang;
  if (x>xx) and (abs(x-xx+360)<abs(xx-x)) then
  dec(p[posi].pt[c].ang,incang) else
  inc(p[posi].pt[c].ang,incang);
  if p[posi].pt[c].ang>360 then p[posi].pt[c].ang:=p[posi].pt[c].ang-360;
  if p[posi].pt[c].ang<0 then p[posi].pt[c].ang:=360+p[posi].pt[c].ang;
end;}

  if abs(abs(p[posi].pt[c].toang)-(abs(p[posi].pt[c].ang)))>incang then begin
  if p[posi].pt[c].toang>p[posi].pt[c].ang then
  inc(p[posi].pt[c].ang,incang) else
  dec(p[posi].pt[c].ang,incang);
  end;
end;

procedure Tfrmabout.FormCreate(Sender: TObject);
begin
inicializa;
end;

procedure Tfrmabout.Button1Click(Sender: TObject);
begin
close;
end;
function tfrmabout.AngleOfPt(x1,y1,x2,y2:integer) : integer;
const RADTODEG = 180 / Pi;
var
  x,y:integer;
begin
Result := 0;
  x:=x1-x2;
  y:=y1-y2;
  if ((x = 0) and (y < 0)) then

    Result := 270 else

  if ((x = 0) and (y > 0)) then

    Result := 90 else

  if ((x > 0) and (y >= 0)) then

    Result := round(ArcTan(y / x) * RADTODEG) else

  if ((x < 0) And (y > 0)) then

    Result := round(180 - (ArcTan(y / Abs(x))* RADTODEG)) else

  if ((x < 0) And (y <= 0)) then

    Result := round(180 + (ArcTan(y / x) * RADTODEG)) else

  if ((x > 0) and (y < 0)) then

    Result := round(360 - (ArcTan(Abs(y) / x) * RADTODEG)) else

  Result:=0;



end;



procedure Tfrmabout.tabMouseMove(Sender: TObject; Shift: TShiftState; X,
  Y: Integer);
  var c,po:integer;

begin
//  label1.caption:=inttostr(angleofpt(x,y,300,300));
  for po:=1 to maxp do
  for c:=1 to maxpt do
    p[po].pt[c].toang:=angleofpt(x,y,p[po].pt[c].x,p[po].pt[c].y);

end;

procedure Tfrmabout.FormClose(Sender: TObject; var Action: TCloseAction);
begin
action:=cafree;
end;

end.
