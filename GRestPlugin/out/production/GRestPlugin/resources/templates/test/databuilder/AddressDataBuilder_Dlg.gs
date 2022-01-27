package resources.templates.test.databuilder;



/**
 * CV9-102 - MOJ Days Off Work value not populating
 * Auto generated Data Builder Class
 *
 * User: AAFY
 * Date: 27/01/2022
 * Time: 12:08:06.943 AM
 */
public class AddressDataBuilder_Dlg {

    public static function create() : Address{
        var _instance = new Address()

        _instance.country="Test country"
        _instance.city="Test city"


        return _instance
    }

}


