import { useQuery } from '@tanstack/react-query';
import { api, type Network } from './api';
export function useNetworkData(){return useQuery({queryKey:['network'],queryFn:()=>api<Network>('/network'),retry:false});}
