		<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
        <%@include file="header.jsp" %>
		
        <jsp:include page="${welcomeMenu}" flush="true" />
     
       <%@include file="footer.jsp" %>
 
      <script>
      function init_gauge() {
			
		if( typeof (Gauge) === 'undefined'){ return; }
		
		console.log('init_gauge [' + $('.gauge-chart').length + ']');
		
		console.log('init_gauge');
		

		  var chart_gauge_settings = {
		  lines: 12,
		  angle: 0,
		  lineWidth: 0.4,
		  pointer: {
			  length: 0.75,
			  strokeWidth: 0.042,
			  color: '#1D212A'
		  },
		  limitMax: 'false',
		  colorStart: '#1ABC9C',
		  colorStop: '#1ABC9C',
		  strokeColor: '#F0F3F3',
		  generateGradient: true
	  };
		
		
		if ($('#chart_gauge_01').length){ 
		
			var chart_gauge_01_elem = document.getElementById('chart_gauge_01');
			var chart_gauge_01 = new Gauge(chart_gauge_01_elem).setOptions(chart_gauge_settings);
			
		}	
		
		
		if ($('#gauge-text').length){ 
		
			chart_gauge_01.maxValue = 100;
			chart_gauge_01.animationSpeed = 32;
			chart_gauge_01.set(35);
			chart_gauge_01.setTextField(document.getElementById("gauge-text"));
		
		}
	}
      </script>
      
      <c:if test="${not empty fn:trim(notification)}">
		<script type="text/javascript">
			$(function(){
		 	 new PNotify({
		        title: '${title}',
		        text: '${message}',
		        type: '${notification}',
		        styling: 'bootstrap3'
		        });
			});
			</script>
		</c:if>
    </body>
</html>