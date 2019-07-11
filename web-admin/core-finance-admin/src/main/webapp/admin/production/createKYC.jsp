<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="header.jsp" %>

        <!-- page content -->
        <div class="right_col" role="main">
          <div class="">
            <div class="page-title">
              <div class="title_left">
                <h3>Member</h3>
              </div>

              <div class="title_right">
                <div class="col-md-5 col-sm-5 col-xs-12 form-group pull-right top_search">
                 <div class="input-group">
                    <br /> <br /> </div>
                </div>
              </div>
            </div>
            <div class="clearfix"></div>
            <div class="row">
              <div class="col-md-12 col-sm-12 col-xs-12">
                <div class="x_panel">
                  <div class="x_title">
                    <h2>Create Member KYC</h2>
                    <ul class="nav navbar-right panel_toolbox">
                      <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                      </li>
                      <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>
                        <ul class="dropdown-menu" role="menu">
                          <li><a href="#">Settings</a>
                          </li>
                        </ul>
                      </li>
                      <li><a class="close-link"><i class="fa fa-close"></i></a>
                      </li>
                    </ul>
                    <div class="clearfix"></div>
                  </div>
                 
                   <div class="x_content">
                    <div>
        		           <p>1. Capture ID Photo :
 		            	</div>
             	    <div>
 					   <video id="videoElement" autoplay></video>
 					   <p><button class="btn btn-success" id="screenshot-button">Capture Image</button>
    					</div>
 	              </div>
                </div>
               </div>

              </div>
            </div>
          </div>
        </div>
        <!-- /page content -->

<%@include file="footer.jsp" %>

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

<script>
	var video = document.querySelector("#videoElement");
	if (navigator.mediaDevices.getUserMedia) {       
    navigator.mediaDevices.getUserMedia({video: true})
  .then(function(stream) {
    video.srcObject = stream;
 	 })
	  .catch(function(err0r) {
	    console.log("Something went wrong!");
	  });
	}
</script>

 <script>
 		$("#grouptable")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "dom" : 'Blfrtip',
     			     "buttons" : [
                      'csvHtml5'
     		          ],
       				 "ajax" : {
       					 "url" : "/admin/groupData",
       					 "data" : function ( d ) {
 		               	  	d.username = "${member.username}";
 		               		}
 			          },
       				 "columns" : [{
								"data" : "id"
							}, {
								"data" : "formattedCreatedDate"
							}, {
								"data" : "name"
							}, {
								"data" : "description"
							}, {
								"data" : "id",
								"render" : function ( data, type, row ) {
                   					 return "<a href='detailGroup?id=" + data + "&username=${member.username}'>Detail</a>";
               					 }
							}]
					});
	</script>
	
	</body>
</html>