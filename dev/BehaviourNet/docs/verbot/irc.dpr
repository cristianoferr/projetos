program irc;

uses
  uirc in 'uirc.pas' {frmirc},
  uabout in 'uabout.pas' {frmabout},
  uverbot in 'uverbot.pas' {frmverbot},
  uentrada in 'uentrada.pas',
  uprocessa in 'uprocessa.pas',
  usaida in 'usaida.pas';

{$R *.RES}

begin
  Application.CreateForm(Tfrmverbot, frmverbot);
end.
