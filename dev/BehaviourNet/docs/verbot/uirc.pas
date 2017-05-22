unit uirc;{
This module was made by Cristiano M. Ferreira,
If you use this module for anything... please let me know.
You may email me if you have any question.
lebeau@linuxbr.com.br
or visit my page at http://lbworld.cjb.net

Originally made in Visual Basic by Dasmius (more info at
ircpre2.txt).


}


interface

uses
  Windows, Messages, SysUtils, Classes, Graphics, Controls, Forms, Dialogs,
  StdCtrls, Menus, ScktComp, uabout,Psock,uverbot;

type  Tfrmirc = class(TForm)
    MainMenu1: TMainMenu;
    Arquivo1: TMenuItem;
    Connectar1: TMenuItem;
    incoming: TMemo;
    namelist: TListBox;
    outgoing: TEdit;
    tcp1: TClientSocket;
    Powersock1: TPowersock;
    topic: TEdit;    About1: TMenuItem;    Memo1: TMemo;    Verbot1: TMenuItem;    procedure AddText(textmsg:String);
    procedure SendData(textmsg:String);
    procedure FormCreate(Sender: TObject);
    procedure incomingChange(Sender: TObject);
    procedure outgoingKeyPress(Sender: TObject; var Key: Char);
    procedure Connectar1Click(Sender: TObject);
    procedure tcp1Connect(Sender: TObject; Socket: TCustomWinSocket);
    procedure tcp1Read(Sender: TObject; Socket: TCustomWinSocket);
    procedure parser(tx:string);
    procedure sendcmd(s:string);
    procedure About1Click(Sender: TObject);
    procedure Verbot1Click(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;
var
  frmirc: Tfrmirc;
  nickname,crlf,oldtext,channel,server:string;
  cmode:integer;// CurrentMode of client
          // 0 is logged in
          // 1 is joining channel
          // 2 is in channel
  port:integer;

implementation
{$R *.DFM}procedure tfrmirc.AddText(textmsg:String);
begin
  // Add the data in textmsg to the Incoming
  // text box and force the text down
  Incoming.Text := Incoming.Text +textmsg + CRLF
End;
procedure tfrmirc.SendData(textmsg:String);
begin
  // Send the data in textmsg to the server, and
  // add a CRLF
  TCP1.Socket.SendText(textmsg+CRLF);
End;
procedure Tfrmirc.Connectar1Click(Sender: TObject);
begin
  If connectar1.Caption = '&Conectar' Then begin
   // Set the RemoteHost to the IRC Server Host
    TCP1.host:=Server;
    // Set the Port to connect to
    TCP1.Port := Port;
    // Connect
    TCP1.active:=true;
    // Clear textbox, topic and listbox
    Incoming.Text:= '';
    NameList.Clear;
    Topic.Text:='';
    AddText('*** Attempting to connect to ' + Server + '...');
    connectar1.Caption:= '&Desconecta';
  end
  Else begin
    connectar1.Caption := '&Conectar';
    AddText('*** Disconnected');
    // Close the socket
    TCP1.active:=false;
    TCP1.Close;
  End;
End;
procedure Tfrmirc.FormCreate(Sender: TObject);
begin
  // Set CRLF to be Cairrage Return + Line Feed,
  // ALL IRC messages end with this
  CRLF := #13+#10;
  // Set the current mode to 0
  CMode := 0;
  //Set the default values
  Server := '127.0.0.1';
  Port := 6667;
  Nickname := 'Terminus';
    Incoming.Text:= '';
    NameList.Clear;
    Topic.Text:='';
    AddText('*** Attempting to connect to ' + Server + '...');
    connectar1.Caption:= '&Desconecta';

end;
procedure Tfrmirc.incomingChange(Sender: TObject);
begin
// We want this box to scroll down automatically.
  Incoming.SelStart := Length(Incoming.Text);
  incoming.sellength:=10;
// What this does is says, make the start of my
// selected text the end of the entire text,
// which effectively scrolls down the textbox,
// but does not select anything. The len()
// command returns the length of characters of
// the text, in a number.
end;
procedure tfrmirc.sendcmd(s:string);
var
  c:integer;
  sender:tobject;
  ch:char;
begin
  outgoing.text:=s;
  ch:=#13;
  outgoingkeypress(sender,ch);
end;
procedure Tfrmirc.outgoingKeyPress(Sender: TObject; var Key: Char);
var
  msg,s:string;
  c,p:integer;
begin
  // Exit unless its a return, then process
  If Key <> #13 Then Exit;
  Key:= #0; // Stop that stupid beep!
  msg := Outgoing.Text;
  If msg[1] <> '/' Then
  begin
   // they want to send a msg, send it if we're
    // in a channel
    If NameList.items.Count > 0 Then begin
      SendData('PRIVMSG ' + channel + ' :' + msg);
      AddText('> ' + msg);
    End;
   end
  Else
  begin
    s:='';
//    msg:=uppercase(msg);
    p:=0;
    for c:=1 to length(msg) do begin
      if p=0 then if msg[c]=' ' then p:=c;
      if p=0 then s:=s+msg[c];
    end;
    delete(msg,1,p);
    if uppercase(s)='/JOIN' THEN BEGIn
        SendData('JOIN ' + msg); CMode := 1; //' join the channel, set the mode
        channel:= msg;
    end else
    if uppercase(s)='/ME' then begin
        // if we're in a channel, then do an action
        If NameList.items.Count > 0 Then SendData('PRIVMSG ' + channel + ' :' + #1 + 'ACTION ' + msg + #1);
        AddText('* ' + Nickname + ' ' + msg);
    end else
    if uppercase(s)='/MSG' then begin
        //send a priv msg
        s:='';
        p:=0;
    for c:=1 to length(msg) do begin
      if p=0 then if msg[c]=' ' then p:=c;
      if p=0 then s:=s+msg[c];
    end;
    delete(msg,1,p);
    SendData('PRIVMSG ' + s + ' :' +msg);
    AddText('=->' + s+ '<-= ' + msg)
    End;
  //' clear the textbox
  Outgoing.Text := '';
  end;
end;



procedure Tfrmirc.tcp1Connect(Sender: TObject; Socket: TCustomWinSocket);
begin//  Physical connect
  AddText('*** Connection established.');
  AddText('*** Sending login information...');
  // Send the server my nickname
  SendData('NICK '+ Nickname);
  // Send the server the user information
  SendData('USER email '+powersock1.LocalIP + ' ' + Server + ' :username');
end;


procedure Tfrmirc.tcp1Read(Sender: TObject; Socket: TCustomWinSocket);var
 s:string;
 i,p:integer;
begin
s:=socket.receivetext;
p:=1;
s:=s+#13;
while length(s)>2 do begin
  parser(copy(s,1,pos(#13,s)));
  delete(s,1,pos(#13,s));
  if length(s)>2 then
  while (s[1]=#13) or (s[1]=#10) do delete(s,1,1);
end;
end;
procedure tfrmirc.parser(tx:string);
var
  indata,sline,msg,msg2:string;
  x,c:integer;
beginindata:=tx;//incoming.lines.add(socket.receivetext);

  // Get the incoming data into a string
  // Add any unprocessed text on first
//  inData := OldText + inData;

  // Some IRC servers are only using a Cairrage
  // Retrun, or a LineFeed, instead of both, so
  // we need to be prepared for that
  x := 1;
{  If pos(crlf,indata)>=1 then x := 1;
  If pos(#10,indata)>=1 Then x := 1;
  If pos(#13,indata)>=1 Then x := 1;
  If x = 1 Then
    OldText := '' // its a full send, process
  Else begin
    OldText := inData;
    exit;
    end;} // incomplete send
          // save and exit
//  if indata[length(indata)]<#32 then delete(indata,length(indata),1);
//  if indata[length(indata)]<#32 then delete(indata,length(indata),1);
  sline:=indata;
  memo1.lines.add(sline);
  for c:=1 to length(sline) do begin

    if sline[c]=#3 then begin
      while (sline[c]=#3) or (sline[c]=',') or (sline[c] in ['0'..'9']) do
       delete(sline,c,1);
    end;
    if sline[c]<#32 then
      delete(sline,c,1);
  end;
  for c:=1 to length(sline) do begin

    if sline[c]=#3 then begin
      while (sline[c]=#3) or (sline[c]=',') or (sline[c] in ['0'..'9']) do
       delete(sline,c,1);
    end;
    if sline[c]<#32 then
      delete(sline,c,1);
  end;

  If pos('PING :',sline)=1 then //  we need to pong to stay alive
  begin
    AddText('PING? PONG!');
    SendData('PONG '+Server);
  End else
  If pos('ERROR',sline)=1 Then // some error
    AddText('*** ERROR '+ copy(sline,pos('(',sline),length(sline))) else
  If pos(':'+nickname,sline)=1 Then begin
    // a command for the client only
    sline:= copy(sline,pos(' ',sline)+ 1,length(sline));
    if pos('MODE',sline)>=1 then
        AddText('*** Your mode is now '+ copy(sline, pos(':',sline)+1,length(sline)));
    if pos('JOIN',sline)>=1 then begin
        AddText('*** Your channel is now '+ copy(sline, pos(':',sline)+1,length(sline)));

    end;
  End;
  if pos('PART #',sline)>=1 then begin
    AddText(copy(sline,2,pos('!',sline)-2) + ' left channel ' + copy(sline, pos('#',sline),length(sline)));
  end;
  if pos('JOIN :#',sline)>=1 then begin
    AddText(copy(sline,2,pos('!',sline)-2) + ' joined channel ' + copy(sline, pos('#',sline),length(sline)));
  end;
  if pos('QUIT :',sline)>=1 then begin
    AddText(copy(sline,2,pos('!',sline)-2) + ' quit: ' + copy(sline, pos('QUIT',sline)+6,length(sline)));
  end;

    if pos('NICK :',sline)>=1 then begin
//  :Satan-tb!~scp58@200.18.12.108 NICK :BrasIRC9633213247361
    AddText(copy(sline,2,pos('!',sline)-2) + ' is now know as ' + copy(sline, pos('NICK :',sline)+6,length(sline)));
  end;

  If pos('PRIVMSG',sline)>=1 then begin
    //someone /msged us
    msg := copy(sline,pos(' ',sline)+9,length(sline));
    If uppercase(copy(msg,1,pos(' ',msg)-1)) = uppercase(Nickname) Then begin
      // add so its: --nick-- msg here
      AddText('--' + copy(sline,2,pos('!',sline)-2) + '-- ' + copy(msg, pos(':',msg)+ 1,length(msg)));
      frmverbot.newmsg(copy(sline,2,pos('!',sline)-2),copy(msg, pos(':',msg)+ 1,length(msg)));
    End;
    if msg[1]='#' then begin
    AddText('<'+copy(sline,2,pos('!',sline)-2)+'->'+copy(msg,1,pos(' ',msg)-1) + '> ' + copy(msg, pos(':',msg)+ 1,length(msg)));
    end;
  End;
  Case CMode of
    0: // not in channel
    begin
      If pos('001 '+nickname,sline)>=1  Then begin
        Server := copy(sline,2,pos('001 '+nickname,sline)-3);
        caption:='Servidor: '+server;
      End;
      If pos(':'+server,sline)>=1 then begin
        // its a server msg, add the important part
        delete(sline,1,1);
        sline := copy(sline, pos(':',sline)+1,length(sline));
        //:washington.dc.us.undernet.org 001 Das2 :Welcome to the Internet Relay Network Das2
        AddText(sline);
      End;
    end;
    1: //' joining channel
    begin
      If pos(':'+Server,sline)>=1 Then begin
        msg := copy(sline, pos(' ',sline)+ 1,length(sline));
      end;
        if pos('332',msg)>=1 then begin// Topic
            Topic.Text := copy(msg, pos(':',msg)+ 1,length(sline));
            sline:=copy(msg,pos(':'+server,msg),length(msg));
        end;

         if (pos('333',msg)<1) and (pos('353',msg)>=1) then begin // Name list
            msg:= copy(msg,pos(' 353 ',msg)+1,length(msg));
            msg:=copy(msg,pos(':',msg)+1,length(msg));
//            msg:=copy(msg,1,pos(#13,msg)-1);
            msg:=msg+' ';
            c:=1;

            while (length(msg)>0) and (c<>length(msg)) do begin
               c:=length(msg);
               namelist.items.add(copy(msg,1,pos(' ',msg)-1));
               delete(msg,1,pos(' ',msg));
            end;
         end;
        if (pos('366',msg)>=1) then begin // Name list

            CMode := 2; // change mode to joined channel
        end;

end;
end;
end;
procedure Tfrmirc.About1Click(Sender: TObject);begin
frmabout:=tfrmabout.create(self);
frmabout.show;
end;

procedure Tfrmirc.Verbot1Click(Sender: TObject);begin
frmverbot:=tfrmverbot.create(self);
frmverbot.show;
        
end;

end.
