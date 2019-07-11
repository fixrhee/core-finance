<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="header.jsp" %>

        <!-- page content -->
        <div class="right_col" role="main">
          <div class="">
            <div class="page-title">
              <div class="title_left">
                <h3>Webservice</h3>
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
                    <h2>Manage Webservice</h2>
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
             	     <div align="right">
             			<p><button onClick="location.href='/admin/createWebservice'" class="btn btn-success">Create New Webservice</button>
             		</div>
             	   <table id="wstable" class="table table-striped table-bordered dt-responsive nowrap" cellspacing="0" width="100%">
                      <thead>
                        <tr>
                          <th>ID</th>
                          <th>Name</th>
                          <th>Username</th>
                          <th>Enabled</th>
                          <th>Secure</th>
                          <th>Action</th>
                        </tr>
                      </thead>                      
                    </table>
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
 		$("#wstable")
				.DataTable(
					{
					 "processing" : true,
       				 "serverSide" : true,
       				 "dom" : 'Blfrtip',
     			     "buttons" : [
                      'csvHtml5'
     		          ],
       				 "ajax" : {
       					 "url" : "/admin/webserviceData",
       					 "data" : function ( d ) {
 		               	}
 			          },
       				 "columns" : [{
								"data" : "id"
							}, {
								"data" : "name"
							}, {
								"data" : "username"
							}, {
								"data" : "active"
							}, {
								"data" : "secureTransaction"
							}, {
								"data" : "id",
								"render" : function ( data, type, row ) {
                   					 return "<a href='webserviceDetail?id=" + data + "' class='btn btn-primary btn-xs'><i class='fa fa-folder'></i> Detail</a>" +
                   					 "<a href='editWebservice?id=" + data + "' class='btn btn-info btn-xs'><i class='fa fa-pencil'></i> Edit</a>";
               					 }
							}]
					});
	</script>
	
	</body>
</html>