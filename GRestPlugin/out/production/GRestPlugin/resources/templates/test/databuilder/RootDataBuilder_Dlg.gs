package resources.templates.test.databuilder;

usesresources.templates.test.Address
usesresources.templates.test.Emails


/**
 * CV9-102 - MOJ Days Off Work value not populating
 * Auto generated Data Builder Class
 *
 * User: AAFY
 * Date: 27/01/2022
 * Time: 12:08:06.943 AM
 */
public class RootDataBuilder_Dlg {

    public static function create() : Root{
        var _instance = new Root()

        _instance.Skills=Skills.create()
        _instance.id=100
        _instance.area="Test area"
        _instance.Address=Address.create()
        _instance.name="Test name"
        _instance.active=false
        _instance.Emails=Emails.create()


        return _instance
    }

}


