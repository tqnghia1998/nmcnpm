using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using ServerApp.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace ServerApp.Data
{
    public class SubjectDbContext : IdentityDbContext<User>
    {
    }
}
