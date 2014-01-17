$(function() {
  $('.story').mouseover(function() {
    // reset
    $('.story').removeClass('related');
    $('.story').removeClass('current');

    $(this).addClass('current');

    var relatedUrls = $(this).data('related').split(' ');
    relatedUrls.forEach(function(relatedUrl) {
      $('[data-url="'+relatedUrl+'"]').addClass('related');
    });
  })
});
