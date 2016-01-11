(function(angular) {

	angular.module('gaintMedia').controller('NewServiceController', controller);

	function controller($scope, $http, $q) {
		var vm = this;
		vm.packets = {};
		vm.contract = null;
		vm.img = null;

		vm.submitStepOne = submitStepOne;
		vm.submitStepTwo = submitStepTwo;
		vm.printAndSave = printAndSave;

		function submitStepOne(form) {
			$http.get('/rest/packet?active=true').success(function(data) {
				vm.packets = data;
			});

			if (form.$valid) {
				vm.secondStep = true;
			}
		}

		function submitStepTwo() {
			$http.post('/rest/contract/', vm.user).success(function(data) {
				vm.contract = data;
				console.log(data);
			});
			vm.thirdStep = true;
		}

		// create pdf
		function createPDF() {
			var form = cache_width = $('#print-form').width();
			getCanvas().then(function(canvas) {
				var img = canvas.toDataURL("image/png"), doc = new jsPDF({
					unit : 'px',
					format : 'a4'
				});
				doc.addImage(img, 'PNG', 0, 0);
				doc.save('ugovor.pdf');
				$('#print-form').removeAttr('style').width(cache_width);
			});
		}

		// create canvas object
		function getCanvas() {
			// var form = $('#print-form'), cache_width = form.width(), a4 = [
			// 730.28, 1110.89 ];
			// $('#print-form').width(a4[0]).css('max-width', 'none');
			// return html2canvas(form, {
			// imageTimeout : 2000,
			// removeContainer : true,
			// });
			return html2canvas($("#print-form"), {
				onrendered : function(canvas) {
					theCanvas = canvas;
				}
			});
		}

		function printAndSave() {

			var deferred = $q.defer();
			$http.post('/rest/contract/', vm.user).success(function(data) {
				deferred.resolve(data);
			})
			.catch(function(err) {
				deferred.reject(err);
			});

			$('body').scrollTop(0);
			createPDF();
			//			
			// var pdf = new jsPDF('p', 'pt', 'a4');
			// var source = $('#print-form')[0];
			//			
			// specialElementHandlers = {
			// '#bypassme': function(element, renderer){
			// return true
			// }
			// }
			// margins = {
			// top: 50,
			// left: 40,
			// width: 280
			// };
			//			
			// pdf.fromHTML(
			// source // HTML string or DOM elem ref.
			// , margins.left // x coord
			// , margins.top // y coord
			// , {
			// 'elementHandlers': specialElementHandlers,
			// 'width': margins.width
			// },
			// function (dispose) {
			// // dispose: object with X, Y of the last line add to the PDF
			// // this allow the insertion of new lines after html
			// pdf.save('html2pdf.pdf');
			// }
			// )
		}

	}

})(angular);