var EX2 = (function () {

    return {

        init: function () {

            $("#version").html("jQuery Version: " + $().jquery);

        },

        submitSearchForm: function () {

            if ($("#search").val() === "") {

                alert("You must enter a search parameter!  Please try again.");
                return false;

            }

            $.ajax({

                url: "main/PersonnelServlet",
                method: "GET",
                data: $('#searchform').serialize(),
                dataType: "html",
                success: function (response) {
                    $("#resultsarea").html(response);
                }

            });

            return false;

        }

    };

}());