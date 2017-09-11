$(document).ready(function(){
	urlsMaisAcesadas();
	carregarErro();
    $('.modal').modal();
});
 
var carregarErro = function(){
	if($("#error").length > 0)
		Materialize.toast($("#error").val(), 4000);	
};

var salvar = function(formId, modalId){

	$("#"+formId).submit(function(e){
		e.preventDefault();

		$.ajax({
			url: $(this).attr('action')+"?"+$(this).serialize(),
			type: $(this).attr('method'),
			cache:false,
			async:false,
			success:function(data, textStatus, JQXhr){
				data = JSON.parse(data);
				console.log(data)
				$('#'+formId).unbind('submit');
				
				if(data.err_code == "001" || data.err_code == "002")
					Materialize.toast(data.description, 4000, 'red');	
				else
					Materialize.toast('Concluído com Sucesso!', 4000, 'green');		

				urlsMaisAcesadas();

				$('#'+modalId).modal('close');
			
			},
			error:function(JQxhr, textStatus, errorThrown){
				Materialize.toast(textStatus, 4000);	
			}
		})
		
		
		this.reset();
		
		return false;
	});

};

var urlsMaisAcesadas = function(){
	$.ajax({
		url: "/mostAccessed",
		type: "GET",
		cache:false,
		success:function(data, textStatus, JQXhr){
			$("#table-encurtador tbody tr").remove();

			data.map(function(val){
				$("#table-encurtador tbody ").append(
						"<tr>" +
						"	<td>"+val.url_original+"</td>" +
						"	<td>"+val.alias+"</td>" +
						"	<td>"+val.access+"</td>" +
						"</tr>");
			});
		},
		error:function(JQxhr, textStatus, errorThrown){
			console.log("erro")
		}
	})

};

