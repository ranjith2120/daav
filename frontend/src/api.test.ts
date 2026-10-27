import { afterEach, describe, expect, it, vi } from 'vitest';
import { api } from './api';

afterEach(() => vi.unstubAllGlobals());

describe('API envelope handling', () => {
  it('returns the data payload from a successful response', async () => {
    const fetchMock = vi.fn().mockResolvedValue(new Response(JSON.stringify({ data: { count: 3 }, meta: { requestId: 'req-1' } }), { status: 200 }));
    vi.stubGlobal('fetch', fetchMock);
    await expect(api<{ count: number }>('/dashboard')).resolves.toEqual({ count: 3 });
    expect(fetchMock).toHaveBeenCalledWith('http://localhost:8080/api/v1/dashboard', expect.objectContaining({ headers: { 'Content-Type': 'application/json' } }));
  });

  it('surfaces the server error message', async () => {
    vi.stubGlobal('fetch', vi.fn().mockResolvedValue(new Response(JSON.stringify({ error: { message: 'Select two locations.' } }), { status: 422 })));
    await expect(api('/routes/shortest-path')).rejects.toThrow('Select two locations.');
  });
});
