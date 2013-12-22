$(document).ready(function(){

    function createPost(){
            var id = $('.content__bio_table .bio_table__row .line__id__editor').map(function(){return $(this).val()})
            id = Math.max.apply(null, id) + 1;

            var bio_table = $(".content__bio_table")
            var line = $("<tr>").addClass("bio_table__row__edit bio_table__row").prependTo(bio_table)
            var cell = $("<td>").addClass("bio_table__year").appendTo(line)
            var content = $("<span>").addClass("line__year").appendTo(cell)
            var content = $("<span>").addClass("line__month").appendTo(cell)
            var input = $("<input>").attr("type","hidden").attr("name","id").attr("value",id).addClass("line__id__editor").appendTo(cell)
            var input = $("<input>").attr("type","text").attr("placeholder","Year").attr("maxlength","4").addClass("line__year__editor").appendTo(cell).focus()
            var input = $("<input>").attr("type","text").attr("placeholder","Month").attr("maxlength","2").addClass("line__month__editor").appendTo(cell)
            var cell = $("<td>").addClass("bio_table__event").appendTo(line)
            var content = $("<span>").addClass("line__event").appendTo(cell)
            var input = $("<textarea>").attr("placeholder","Input an event").addClass("line__event__editor").appendTo(cell)
            var cell = $("<td>").addClass("bio_table__buttons").appendTo(line)
            var edit = $("<a>").attr("href","#").addClass("line__buttons__edit").appendTo(cell).text('Редактировать')
            var save = $("<a>").attr("href","#").addClass("line__buttons__save").appendTo(cell).text('Сохранить')
            var br = $("<br>").appendTo(cell)
            var del = $("<a>").attr("href","#").addClass("line__buttons__delete").appendTo(cell).text('Удалить')
            return line
        }

	$(".new__post").click(createPost)

	function sort() {
		 data = $('.content__bio_table .bio_table__row').sort(function(i,e){
			
			a = parseInt($(i).find('.line__year').text(),10) *12 + parseInt($(i).find('.line__month').text(),10)
			b = parseInt($(e).find('.line__year').text(),10) *12 + parseInt($(e).find('.line__month').text(),10)
			if (a==b) {
				return 0
			}
			if (a<b){
				return 1
			} else {
				return -1
			}

		}).each(function() {
			$('.content__bio_table').append(this)
		}).map(function() {
            var tr = $(this)
            var year = tr.find('.line__year').text()
            var month = tr.find('.line__month').text()
            var text = tr.find('.line__event').text()
            var id = tr.find('.line__id__editor').val()
            return {year:year,month:month,text:text, id:id}
		})

//		var json = $.toJSON(data.get())
//		$.cookie("bio",json)
	}

	sort()

	$('.content__bio_table').on('click','.line__buttons__save', function(e){
		var tr = $(this).parents('.bio_table__row').removeClass('bio_table__row__edit')
		var year = tr.find('.line__year__editor').val()
		var month = tr.find('.line__month__editor').val()
		var text = tr.find('.line__event__editor').val()
		var id = tr.find('.line__id__editor').val()
		tr.find('.line__year').text(year)
		tr.find('.line__month').text(month)
		tr.find('.line__event').text(text)
		sort()


		$.ajax('/api',{method:'post', data:{year:year, month:month, text:text, id:id}})
	})

	$('.content__bio_table').on('click','.line__buttons__edit', function(e){
		var tr = $(this).parents('.bio_table__row').addClass('bio_table__row__edit')
		var year = tr.find('.line__year').text()
		var month = tr.find('.line__month').text()
		var text = tr.find('.line__event').text()
		tr.find('.line__year__editor').val(year)
		tr.find('.line__month__editor').val(month)
		tr.find('.line__event__editor').val(text)
	})

	$('.content__bio_table').on('click','.line__buttons__delete', function(e){
		if (confirm('Are you sure?')) {
			var tr= $(this).parents('.bio_table__row')
            var id = tr.find('.line__id__editor').val()
			tr.remove()
            $.ajax('/api',{method:'delete', data:{id:id}})

			sort()
		}
	})
})