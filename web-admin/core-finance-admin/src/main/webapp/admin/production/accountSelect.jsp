<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="header.jsp" %>

        <!-- page content -->
        <div class="right_col" role="main">
          <div class="">
            <div class="page-title">
              <div class="title_left">
                <h3>Transaction History</h3>
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
               
                
                <!-- Account pickers -->
                <div class="x_panel" style="">
                  <div class="x_title">
                    <h2>Select Account</h2>
                    <ul class="nav navbar-right panel_toolbox">
                      <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a>
                      </li>
                      <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><i class="fa fa-wrench"></i></a>
                        <ul class="dropdown-menu" role="menu">
                          <li><a href="#">Settings 1</a>
                          </li>
                        </ul>
                      </li>
                      <li><a class="close-link"><i class="fa fa-close"></i></a>
                      </li>
                    </ul>
                    <div class="clearfix"></div>
                  </div>
                  <div class="x_content">
	
				<!-- Account List -->
                     <c:forEach var="acountList" items="${account}">
					   <div class="well" style="overflow: auto">${acountList}</div>
					</c:forEach>
                    
                  </div>
                </div>
               </div>
              </div>
              
             </div>
            </div>
        <!-- /page content -->

<%@include file="footer.jsp" %>