# 🏗️ **CASBIN SIMPLIFIED ARCHITECTURE**

## ✅ **ARCHITECTURE OVERVIEW**

Kiến trúc đã được đơn giản hóa theo yêu cầu - **loại bỏ hoàn toàn Redis caching complexity** và chỉ sử dụng **database + simple event-driven full reload** với **retry mechanism**.

### **📊 Flow Diagram:**

```
┌─────────────┐    ┌──────────────┐    ┌─────────────────┐
│ Auth Service│    │   Database   │    │ Other Services  │
│             │    │   (Source    │    │                 │
│ CRUD Perms  │    │  of Truth)   │    │ Listen Redis    │
│      │      │    │      │       │    │       │         │
│      v      │    │      │       │    │       v         │
│ Publish     │────┤      │       │    │ "RELOAD_POLICIES"│
│"RELOAD_     │    │      │       │    │       │         │
│POLICIES"    │    │      │       │    │ Full Reload     │
└─────────────┘    │      v       │    │ from DB         │
                   │ policy_rules │    │ (with retry)    │
                   │     view     │    └─────────────────┘
                   └──────────────┘
```

### **🎯 Benefits of Simplified Approach:**

- ✅ **No Redis sync complexity** - single source of truth (database)
- ✅ **Ultra simple events** - just "RELOAD_POLICIES" string message
- ✅ **No complex event filtering** - all services reload when needed
- ✅ **Simpler error handling** - clear retry mechanism
- ✅ **Lower latency** - no intermediate caching layer
- ✅ **Easier debugging** - straightforward flow
- ✅ **Memory efficiency** - Casbin enforcer caches in memory

---

## 🧩 **COMPONENTS IMPLEMENTED**

### **1. Database Layer**

```sql
-- V9__create_policy_rule_view.sql (simplified)
CREATE VIEW policy_rules AS
SELECT
    p.id, p.role_id, r.code as resource_code, a.code as action_code
FROM permissions p
JOIN resources r ON p.resource_id = r.id
JOIN actions a ON p.action_id = a.id;
```

### **2. Core Models**

- **`PolicyRule`**: `{id, roleId, resourceCode, actionCode}` - minimal fields only
  - Method: `toCasbinPolicy()` → `[roleId, resourceCode, actionCode]`

### **3. Repository Layer**

- **`AuthSchemaRepository`**: Cross-schema và local queries (no timestamps)
  - `findPermissionsByResourceCodes()` - other services
  - `findPermissionsByResourceCodesLocal()` - auth service

### **4. Service Layer**

- **`PolicyLoader`**: Database-only loading (no Redis)
  - Method: `loadPolicies(enforcer)` - clear + reload from DB
  - Resource filtering based on service configuration
- **`PolicyUpdateService`**: Simple event handling với retry
  - Method: `reloadPoliciesFromEvent(context)` - handle simple string events
  - Scheduled retry mechanism (5 phút × 3 lần)

### **5. Simple Event System**

- **`PolicyEventPublisher`**: Publishes simple "RELOAD_POLICIES" message
- **`PolicyEventListener`**: Listens for string messages, triggers reload
- **No complex event types** - just reload signal

### **6. Configuration**

- **`CasbinProperties`**: Simplified properties
- **`CasbinConfig`**: Database-only configuration với simple event listening

---

## ⚙️ **CONFIGURATION EXAMPLE**

```properties
# Service identification
app.casbin.service-name=auth-service
app.casbin.resources=users,roles,permissions,groups,sessions,actions,resources

# Policy filtering
app.casbin.enable-filtering=true

# Simple event system
app.casbin.redis-channel=casbin:policy:changes
app.casbin.auto-reload=true

# Retry mechanism
app.casbin.retry-count=3
app.casbin.retry-interval-minutes=5
```

---

## 🚀 **RUNTIME FLOW**

### **Startup:**

1. **Service starts** → `CasbinConfig.loadInitialPolicies()`
2. **Load from database** → `PolicyLoader.loadPolicies(enforcer)`
3. **Resource filtering** → Only load relevant policies
4. **Cache in memory** → Casbin enforcer holds policies

### **Runtime Updates (SIMPLIFIED):**

1. **Auth service CRUD** → Database changes
2. **Publish simple event** → `"RELOAD_POLICIES"` to Redis channel
3. **All services receive** → `PolicyEventListener`
4. **Full reload** → `PolicyUpdateService.reloadPoliciesFromEvent()`
5. **Retry if fails** → 5 minutes, max 3 times

### **Event Publishing Example:**

```java
// Auth service - when permissions change
@Autowired
private PolicyEventPublisher publisher;

public void updatePermission(...) {
    // Update database
    permissionRepository.save(permission);

    // Trigger reload for all services
    publisher.publishReloadEvent("Permission updated");
}
```

### **Event Listening:**

```java
// All services receive
// "RELOAD_POLICIES" or "RELOAD_POLICIES:Permission updated"
// → trigger full reload from database
```

---

## 🛡️ **AUTHORIZATION USAGE**

```java
@CasbinAuthorize(
    subject = "#{currentUser.roleId}",
    object = "users",
    action = "read"
)
public List<User> getAllUsers() {
    // Method implementation
}
```

### **Policy Format in Database:**

```
[roleId, resourceCode, actionCode]
Examples:
[1, "users", "read"]
[1, "users", "write"]
[2, "permissions", "read"]
```

---

## 📊 **MONITORING & HEALTH**

### **Health Checks:**

- `PolicyLoader.isHealthy()` - Database connectivity
- `PolicyUpdateService.isHealthy()` - Retry state
- `PolicyUpdateService.getRetryStatus()` - Detailed status

### **Metrics:**

- Policy count: `getCurrentPolicyCount(enforcer)`
- Database count: `getDatabasePolicyCount()`
- Resource-specific count: `getDatabasePolicyCountForResources()`

---

## 🔧 **DEVELOPMENT NOTES**

### **Service Configuration Examples:**

**Auth Service:**

```properties
app.casbin.service-name=auth-service
app.casbin.resources=users,roles,permissions,groups,sessions,actions,resources
```

**User Service:**

```properties
app.casbin.service-name=user-service
app.casbin.resources=users,profiles
```

**Order Service:**

```properties
app.casbin.service-name=order-service
app.casbin.resources=orders,payments,shipping
```

### **Simple Event Publishing (Auth Service Only):**

```java
// When permissions change
policyEventPublisher.publishReloadEvent("Role permission updated");

// Or with channel
policyEventPublisher.publishReloadEvent("casbin:policy:changes", "Bulk update");
```

---

## ✅ **IMPLEMENTATION STATUS**

### **✅ COMPLETED:**

- [x] Database view (simplified, no timestamps)
- [x] PolicyRule model (minimal fields only)
- [x] AuthSchemaRepository (no timestamps in queries)
- [x] PolicyLoader (database-only)
- [x] PolicyUpdateService (simple string event handling)
- [x] PolicyEventPublisher (simple message publisher)
- [x] PolicyEventListener (string message listener)
- [x] CasbinConfig integration
- [x] Configuration properties
- [x] Health checks và monitoring

### **🎯 ULTRA SIMPLIFIED & READY:**

- ✅ **No complex events** - just "RELOAD_POLICIES" string
- ✅ **No event filtering** - all services reload (acceptable with 100K policies)
- ✅ **No timestamps** - database has them if needed
- ✅ **Minimal models** - only essential fields
- ✅ **Clear retry logic** - 5 min × 3 times
- ✅ **Production ready** - comprehensive logging

---

**💡 Key Insight:** _Cực kỳ đơn giản hóa thành công! Từ complex event system với filtering xuống simple "RELOAD_POLICIES" string message. Architecture bây giờ extremely simple, maintainable và đủ robust cho production._
