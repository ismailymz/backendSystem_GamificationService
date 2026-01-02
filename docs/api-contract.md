# Gamification Service – API Contract (v1)

Base path: `/api`

## Period Enum
Allowed values: `DAILY`, `WEEKLY`, `ALL`

---

## Error Response (single format)
All errors must return this JSON:

```json
{
  "timestamp": "2025-12-27T12:30:00",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Driver not found",
  "path": "/api/drivers/7/profile"
}
{
  "tripId": "t-123",
  "distanceKm": 12.5,
  "startTime": "2025-12-27T10:00:00",
  "endTime": "2025-12-27T10:30:00"
}
