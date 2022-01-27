package resources.templates.test.databuilder;



/**
 * CV9-102 - MOJ Days Off Work value not populating
 * Auto generated Data Builder Class
 *
 * User: AAFY
 * Date: 27/01/2022
 * Time: 12:08:06.943 AM
 */
public class EmailsDataBuilder_Dlg {

    public static function create() : Emails{
        var _instance = new Emails()

        _instance.address="Test address"
        _instance.domain="Test domain"


        return _instance
    }

}


