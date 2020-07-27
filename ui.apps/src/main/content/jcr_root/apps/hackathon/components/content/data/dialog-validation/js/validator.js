console.log('loaded');
$(window).adaptTo('foundation-registry').register('foundation.validation.validator',{
    selector: '#email,#name',
    validate: function(e){
        if(e.id==='email'){
          var input = e.value;
          var pattern = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
          if(!pattern.test(input)){
              return 'Please enter a valid email';
          }
        }
       if(e.id==='name'){
          var input = e.value;
          console.log(input);
           console.log(input.length);
           console.log(isNaN(input));
          if(input.length < 3 || !isNaN(input)){
              return 'Please enter a valid name';
          }
        }
    }
});
