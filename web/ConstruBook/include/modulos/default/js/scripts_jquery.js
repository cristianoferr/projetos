$(document).ready(function () {
    $('.currency').maskMoney({thousands: '.', decimal: ','});
});

(function ($) {

    $.fn.formatCurrency = function (settings) {
        settings = jQuery.extend({
            name: "formatCurrency",
            useHtml: false,
            symbol: '',
            global: true
        }, settings);
        return this.each(function () {
            var num = "0";
            num = $(this)[settings.useHtml ? 'html' : 'val']();
            num = num.replace(/\$|\,/g, '');
            if (isNaN(num))
                num = "0";
            sign = (num == (num = Math.abs(num)));
            num = Math.floor(num * 100 + 0.50000000001);
            cents = num % 100;
            num = Math.floor(num / 100).toString();
            if (cents < 10)
                cents = "0" + cents;
            for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++)
                num = num.substring(0, num.length - (4 * i + 3)) + ',' + num.substring(num.length - (4 * i + 3));
            var n = (((sign) ? '' : '-') + settings.symbol + num + '.' + cents);
            //$(this).val('aaa');
            $(this)[settings.useHtml ? 'html' : 'val'](((sign) ? '' : '-') + settings.symbol + num + '.' + cents);
        });
    };
    // Remove all non numbers from text
    $.fn.toNumber = function (settings) {
        settings = jQuery.extend({
            name: "toNumber",
            useHtml: false,
            global: true
        }, settings);
        return this.each(function () {
            var method = settings.useHtml ? 'html' : 'val';
            $(this)[method]($(this)[method]().replace(/[^\d\.]/g, ''));
        });
    };
})(jQuery);