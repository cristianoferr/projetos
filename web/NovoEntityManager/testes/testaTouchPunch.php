
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">

        <title>jQuery UI Touch Punch - Mobile Device Touch Event Support for jQuery UI</title>

        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="">
        <meta name="author" content="">

        <!--[if lt IE 9]>
        <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->

        <link rel="icon" href="/ico/favicon.ico">
        <link rel="apple-touch-icon-precomposed" sizes="114x114" href="/ico/apple-touch-icon-114-precomposed.png">
        <link rel="apple-touch-icon-precomposed" sizes="72x72" href="/ico/apple-touch-icon-72-precomposed.png">
        <link rel="apple-touch-icon-precomposed" href="/ico/apple-touch-icon-57-precomposed.png">

        <link href="http://code.jquery.com/ui/1.8.21/themes/base/jquery-ui.css" rel="stylesheet">
        <link href="http://code.jquery.com/ui/1.8.21/themes/ui-lightness/jquery-ui.css" rel="stylesheet">
        <style>body { background:#fff; font-family:"Helvetica Neue",Helvetica,Arial,sans-serif; }</style>

        <script src="http://127.0.0.1/js/default/jquery-2.0.3.min.js"></script>
        <script src="http://127.0.0.1/js/default/jquery-1.10.2-ui.min.js"></script>
        <script src="http://127.0.0.1/js/default/barometer.js"></script>
        <script src="http://127.0.0.1/js/default/jquery.top_menu.js"></script>
        <script src="http://127.0.0.1/js/default/menu.js"></script>
        <script src="http://127.0.0.1/js/default/seletor.js">
        </script><script src="http://127.0.0.1/js/default/scripts_ajax.js"></script>
        <script src="http://127.0.0.1/js/default/scripts_jquery.js"></script>
        <script src="http://127.0.0.1/js/default/jquery.maskMoney.js"></script>
        <script src="http://127.0.0.1/bootstrap/js/bootstrap.js"></script>
        <script src="http://127.0.0.1/js/default/scripts.js"></script>
        <script src="http://127.0.0.1/js/diagramas/kinetic-v4.7.2.min.js"></script>
        <script src="http://127.0.0.1/js/diagramas/DragDrop.js"></script>
        <script src="http://127.0.0.1/js/diagramas/jquery.ui.touch-punch.min.js"></script>
    </head>
    <body>

        <div class="container">


            <style>
                #draggable { width: 150px; height: 150px; padding: 0.5em; }
            </style>
            <script>
        $(function() {
            $("#draggable").draggable();
        });
            </script>



            <div class="demo">

                <div id="draggable" class="ui-widget-content">
                    <p>Drag me around</p>
                </div>

            </div><!-- End demo -->



            <div class="demo-description">
                <p>Delay the start of dragging for a number of milliseconds with the <code>delay</code> option; prevent dragging until the cursor is held down and dragged a specifed number of pixels with the <code>distance</code> option. </p>
            </div><!-- End demo-description -->



        </div>

        <script type="text/javascript">

            var _gaq = _gaq || [];
            _gaq.push(['_setAccount', 'UA-29418466-1']);
            _gaq.push(['_setDomainName', 'furf.com']);
            _gaq.push(['_trackPageview']);

            (function() {
                var ga = document.createElement('script');
                ga.type = 'text/javascript';
                ga.async = true;
                ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                var s = document.getElementsByTagName('script')[0];
                s.parentNode.insertBefore(ga, s);
            })();

        </script>

    </body>
</html>
