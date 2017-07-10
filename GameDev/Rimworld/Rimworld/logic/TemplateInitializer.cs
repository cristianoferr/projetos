
using Rimworld.model;
using Rimworld.model.components;
using Rimworld.model.entities;
using Rimworld.model.io;
namespace Rimworld.logic
{
    public class TemplateInitializer
    {
        public TemplateInitializer(DataHolder dataHolder)
        {
            this.dataHolder = dataHolder;
            dataHolder.templates = Templates.LoadSaved();
            //if (dataHolder.templates == null)
            {
                dataHolder.templates = new Templates();
                InitializeTemplates();
            }
        }

        private void InitializeTemplates()
        {
            InitHumanoidTemplate();




        }

        private void InitHumanoidBodyParts()
        {
            Template template = templates.CreateTemplate(GameConsts.TEMPL_HUMANOID_BODYPARTS);
            template.AddTag(GameConsts.TAG_BODYPARTS);
            template.AddTag(GameConsts.TAG_HUMANOID);
            template.entityToSpawn = typeof(BodyPartsComponent);

            template.AddComponentWithTags(GameConsts.TAG_ORGAN, GameConsts.TAG_BRAIN).name = "BRAIN";
            template.AddComponentWithTags(GameConsts.TAG_ORGAN, GameConsts.TAG_HEART).name = "HEART";

            Template templ = AddTemplate("brain", typeof(BodyPartsComponent), GameConsts.TAG_ORGAN, GameConsts.TAG_BRAIN);
            templ = AddTemplate("heart", typeof(HeartComponent), GameConsts.TAG_ORGAN, GameConsts.TAG_HEART);

        }

        private Template AddTemplate(string name, System.Type type, string tag1, string tag2)
        {
            Template templ = templates.CreateTemplate("brain");
            templ.AddTag(tag1);
            templ.AddTag(tag2);
            templ.entityToSpawn = type;
            return templ;
        }

        private void InitHumanoidTemplate()
        {
            Template templateHumanoid = templates.CreateTemplate(GameConsts.TEMPL_HUMANOID);
            templateHumanoid.AddTag(GameConsts.TAG_HUMANOID);
            templateHumanoid.AddTag(GameConsts.TAG_ORGANIC);
            templateHumanoid.AddTag(GameConsts.TAG_PHYSICAL);
            templateHumanoid.entityToSpawn = typeof(HumanoidEntity);

            Property propBodyParts = templateHumanoid.AddComponent(GameConsts.TEMPL_HUMANOID_BODYPARTS);
            propBodyParts.AddTag(GameConsts.TAG_BODYPARTS);
            propBodyParts.AddTag(GameConsts.TAG_HUMANOID);

            Property propTraitManager = templateHumanoid.AddComponent("TraitManager");
            propTraitManager.AddTag(GameConsts.TAG_TRAITMANAGER);
            propTraitManager.AddTag(GameConsts.TAG_HUMANOID);

            InitHumanoidBodyParts();
            InitTraitManager();

        }

        private void InitTraitManager()
        {
            Template template = templates.CreateTemplate("TraitManager");
            template.AddTag(GameConsts.TAG_TRAITMANAGER);
            template.AddTag(GameConsts.TAG_HUMANOID);
            template.entityToSpawn = typeof(TraitManagerComponent);

            template.AddIntValue(GameConsts.VAL_POINTS_TO_DISTRIBUTE, 20, 30);

        }
        private Templates templates { get { return dataHolder.templates; } }
        private DataHolder dataHolder;
    }
}
