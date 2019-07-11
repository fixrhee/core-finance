<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Doku Pay Dashboard | </title>

    <!-- Bootstrap -->
    <link href="vendors/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="vendors/font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <!-- NProgress -->
    <link href="vendors/nprogress/nprogress.css" rel="stylesheet">
    <!-- iCheck -->
    <link href="vendors/iCheck/skins/flat/green.css" rel="stylesheet">
	
    <!-- bootstrap-progressbar -->
    <link href="vendors/bootstrap-progressbar/css/bootstrap-progressbar-3.3.4.min.css" rel="stylesheet">
    <!-- JQVMap -->
    <link href="vendors/jqvmap/dist/jqvmap.min.css" rel="stylesheet"/>
    <!-- iCheck -->
    <link href="vendors/iCheck/skins/flat/green.css" rel="stylesheet">
    <!-- bootstrap-wysiwyg -->
    <link href="vendors/google-code-prettify/bin/prettify.min.css" rel="stylesheet">
    <!-- Select2 -->
    <link href="vendors/select2/dist/css/select2.min.css" rel="stylesheet">
    <!-- Switchery -->
    <link href="vendors/switchery/dist/switchery.min.css" rel="stylesheet">
    <!-- starrr -->
    <link href="vendors/starrr/dist/starrr.css" rel="stylesheet">
    
    <!-- PNotify -->
    <link href="vendors/pnotify/dist/pnotify.custom.min.css" rel="stylesheet">
    
    <!-- bootstrap-daterangepicker -->
    <link href="vendors/bootstrap-daterangepicker/daterangepicker.css" rel="stylesheet">
	<!-- bootstrap-datetimepicker -->
    <link href="vendors/bootstrap-datetimepicker/build/css/bootstrap-datetimepicker.css" rel="stylesheet">

 	<!-- Datatables -->
    <link href="vendors/datatables.net-bs/css/dataTables.bootstrap.min.css" rel="stylesheet">
    <link href="vendors/datatables.net-buttons-bs/css/buttons.bootstrap.min.css" rel="stylesheet">
    <link href="vendors/datatables.net-fixedheader-bs/css/fixedHeader.bootstrap.min.css" rel="stylesheet">
    <link href="vendors/datatables.net-responsive-bs/css/responsive.bootstrap.min.css" rel="stylesheet">
    <link href="vendors/datatables.net-scroller-bs/css/scroller.bootstrap.min.css" rel="stylesheet">

    <!-- Custom Theme Style -->
    <link href="build/css/custom.min.css" rel="stylesheet">
    
    <!-- Image Zoom -->
    <style>
    	#myImg, #myImg2, #myImg3 {
		    border-radius: 5px;
		    cursor: pointer;
		    transition: 0.3s;
		}
		
		#myImg #myImg2, #myImg3 :hover {opacity: 0.7;}
		
		/* The Modal (background) */
		.modal {
		    display: none; /* Hidden by default */
		    position: fixed; /* Stay in place */
		    z-index: 1; /* Sit on top */
		    padding-top: 100px; /* Location of the box */
		    left: 0;
		    top: 0;
		    width: 100%; /* Full width */
		    height: 100%; /* Full height */
		    overflow: auto; /* Enable scroll if needed */
		    background-color: rgb(0,0,0); /* Fallback color */
		    background-color: rgba(0,0,0,0.9); /* Black w/ opacity */
		}
		
		/* Modal Content (image) */
		.modal-content {
		    margin: auto;
		    display: block;
		    width: 80%;
		    max-width: 500px;
		}
		
		/* Caption of Modal Image */
		#caption {
		    margin: auto;
		    display: block;
		    width: 80%;
		    max-width: 500px;
		    text-align: center;
		    color: #ccc;
		    padding: 10px 0;
		    height: 150px;
		}
		
		/* Add Animation */
		.modal-content, #caption {    
		    -webkit-animation-name: zoom;
		    -webkit-animation-duration: 0.6s;
		    animation-name: zoom;
		    animation-duration: 0.6s;
		}
		
		@-webkit-keyframes zoom {
		    from {-webkit-transform:scale(0)} 
		    to {-webkit-transform:scale(1)}
		}
		
		@keyframes zoom {
		    from {transform:scale(0)} 
		    to {transform:scale(1)}
		}
		
		/* The Close Button */
		.close {
		    position: absolute;
		    top: 15px;
		    right: 35px;
		    color: #f1f1f1;
		    font-size: 40px;
		    font-weight: bold;
		    transition: 0.3s;
		}
		
		.close:hover,
		.close:focus {
		    color: #bbb;
		    text-decoration: none;
		    cursor: pointer;
		}
		
		/* 100% Image Width on Smaller Screens */
		@media only screen and (max-width: 700px){
		    .modal-content {
		        width: 100%;
		    }
		}
		
	#videoElement {
    width: 500px;
    height: 375px;
    background-color: #666;
	}
    </style>

  </head>

  <body class="nav-md">
    <div class="container body">
      <div class="main_container">
        <div class="col-md-3 left_col">
          <div class="left_col scroll-view">
            <div class="navbar nav_title" style="border: 0;">
              <a href="index" class="site_title"><img src="images/dokuwh.png"></img><span> Doku Pay</span></a>
            </div>

            <div class="clearfix"></div>

            <!-- menu profile quick info -->
            <div class="profile clearfix">
              <div class="profile_pic">
                <img src="images/user.png" alt="..." class="img-circle profile_img">
              </div>
              <div class="profile_info">
                <span>Welcome to DOKU</span>
                <h2>${member.name}</h2>
              </div>
            </div>
            <!-- /menu profile quick info -->

            <br />

            <!-- sidebar menu -->
            <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
              
                <c:forEach var="menuValue" items="${mainMenu}">
					  <div class="menu_section">${menuValue}</div>
				</c:forEach>
             
            </div>
            <!-- /sidebar menu -->

            <!-- /menu footer buttons -->
            <div class="sidebar-footer hidden-small">
              <a href="setting" data-toggle="tooltip" data-placement="top" title="Setting">
                <span class="glyphicon glyphicon-cog" aria-hidden="true"></span>
              </a>
              <a data-toggle="tooltip" data-placement="top" onclick='fullscreen();' title="FullScreen">
                <span class="glyphicon glyphicon-fullscreen" aria-hidden="true"></span>
              </a>
              <a data-toggle="tooltip" data-placement="top" title="Help">
                <span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span>
              </a>
              <a data-toggle="tooltip" data-placement="top" title="Logout" href="logout">
                <span class="glyphicon glyphicon-off" aria-hidden="true"></span>
              </a>
            </div>
            <!-- /menu footer buttons -->
          </div>
        </div>

        <!-- top navigation -->
        <div class="top_nav">
          <div class="nav_menu">
            <nav>
              <div class="nav toggle">
                <a id="menu_toggle"><i class="fa fa-bars"></i></a>
              </div>

              <ul class="nav navbar-nav navbar-right">
                <li class="">
                  <a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
                    <img src="images/user.png" alt="">${member.name}
                    <span class=" fa fa-angle-down"></span>
                  </a>
                  <ul class="dropdown-menu dropdown-usermenu pull-right">
                  	<li><a href="changeCredential"><i class="fa fa-unlock pull-right"></i> Change PIN</a></li>
                    <li><a href="setting"><i class="fa fa-gear pull-right"></i> Setting</a></li>
                    <li><a href="logout"><i class="fa fa-sign-out pull-right"></i> Log Out</a></li>
                   </ul>
                </li>

                <li role="presentation" class="dropdown">
                  <a href="javascript:;" class="dropdown-toggle info-number" data-toggle="dropdown" aria-expanded="false">
                    <i class="fa fa-envelope-o"></i>
                    <span class="badge bg-green">${unreadMessage}</span>
                  </a>
                  <ul id="menu1" class="dropdown-menu list-unstyled msg_list" role="menu">
      			      <c:forEach var="messageValue" items="${messageSummary}">
						  <li>${messageValue}</li>
				    	</c:forEach>
                    <li>
                      <div class="text-center">
                        <a href="message">
                          <strong>See All Messages</strong>
                          <i class="fa fa-angle-right"></i>
                        </a>
                      </div>
                    </li>
                  </ul>
                </li>
              </ul>
            </nav>
          </div>
        </div>
        <!-- /top navigation -->