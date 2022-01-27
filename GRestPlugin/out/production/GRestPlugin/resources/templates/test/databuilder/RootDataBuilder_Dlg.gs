package resources.templates.test.databuilder;

uses resources.templates.test.Address
uses resources.templates.test.Emails


/**
 * CV9-102 - MOJ Days Off Work value not populating
 * Auto generated Data Builder Class
 *
 * User: AAFY
 * Date: 27/01/2022
 * Time: 12:16:12.109 AM
 */
public class RootDataBuilder_Dlg {

    public static function create() : Root{
        var _instance = new Root()

        _instance.Skills=Skills.create()
        _instance.id=0
        _instance.area="Test technology"
        _instance.Address=Address.create()
        _instance.name="Test "
        _instance.active=true
        _instance.Emails=Emails.create()


        return _instance
    }

}


