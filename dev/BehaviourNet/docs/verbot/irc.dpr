program irc;

uses  Forms,
  uirc in 'uirc.pas' {frmirc},
  uabout in 'uabout.pas' {frmabout},
  uverbot in 'uverbot.pas' {frmverbot},
  uentrada in 'uentrada.pas',
  uprocessa in 'uprocessa.pas',
  usaida in 'usaida.pas';

{$R *.RES}

begin  Application.Initialize;
  Application.CreateForm(Tfrmverbot, frmverbot);  Application.Run;
end.

