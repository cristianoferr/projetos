using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(BehaviourNet.Startup))]
namespace BehaviourNet
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
