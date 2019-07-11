<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="header.jsp" %>

        <!-- page content -->
        <div class="right_col" role="main">
          <div class="">
            <div class="page-title">
              <div class="title_left">
                <h3>Access</h3>
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
                    <h2>Change PIN</h2>
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
                     <form id="changecredentialform" data-parsley-validate class="form-horizontal form-label-left" action="/admin/changeCredentialResult" method="POST" modelAttribute="changecredential" >
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">Username :</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="text" id="msisdn" name="msisdn" value="${username}" readonly required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="name">Old PIN :</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="password" id="oldCredential" name="oldCredential" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      <div class="form-group">
                        <label class="control-label col-md-3 col-sm-3 col-xs-12" for="first-name">New PIN :</span>
                        </label>
                        <div class="col-md-6 col-sm-6 col-xs-12">
                          <input type="password" id="newCredential" name="newCredential" required="required" class="form-control col-md-7 col-xs-12">
                        </div>
                      </div>
                      
                      <div class="form-group">
                          <input id="username" name="username" value="${member.username}" class="date-picker form-control col-md-7 col-xs-12" type="hidden">
                      </div>
                      
                      <div class="ln_solid"></div>
                      <div class="form-group">
                        <div class="col-md-6 col-sm-6 col-xs-12 col-md-offset-3">
                          <button name="reset" id="reset" class="btn btn-primary" type="reset">Reset</button>
		                  <button name="submit" id="submit" type="submit" class="btn btn-success">Submit</button>
                        </div>
                      </div>
                    </form>
                    
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

	</body>
</html>